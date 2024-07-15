package de.festivalgpt.backend.scheduler

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.repository.*
import de.festivalgpt.backend.service.*
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FestivalUpdateScheduler(
    private val festivalService: FestivalService,
    private val festivalRepository: FestivalRepository,
    private val cityService: CityService,
    private val cityRepository: CityRepository,
    private val weatherService: WeatherService,
    private val openMeteoClient: OpenMeteoClient
) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val isRunning = AtomicBoolean(false)
  private val festivalsToUpdate = mutableListOf<Festival>()

  @Scheduled(fixedRate = 2000)
  fun update() = runBlocking {
    if (isRunning.compareAndSet(false, true)) {
      try {
        val festival = pullFestival() ?: return@runBlocking
        updateFestivalAndCity(festival)
      } finally {
        isRunning.set(false)
      }
    } else {
      logger.info("Previous update task is still running. Skipping this run.")
    }
  }

  fun pullFestival(): Festival? {
    if (festivalsToUpdate.isEmpty()) {
      festivalsToUpdate.addAll(festivalService.findAllFestivals().sortedBy { it.lastUpdated })
    }
    return festivalsToUpdate.removeFirstOrNull()
  }

  fun updateFestivalAndCity(festival: Festival) = runBlocking {
    val city = festival.postalCode.city

    logger.info("Updating festival ${festival.name} and city ${city.name}")

    if (city.latitude == null || city.longitude == null) {
      if (!updateCoordinates(city)) {
        city.enabled = false
      }
    }

    festival.city = city.name
    festival.countryCode = city.country.code.uppercase()
    festival.enabled = city.enabled
    festival.latitude = city.latitude
    festival.longitude = city.longitude
    festival.lastUpdated = LocalDate.now()

    if (city.enabled) {
      updateWeatherData(city, festival)
    }

    festivalRepository.save(festival)
    cityRepository.save(city)
  }

  fun updateCoordinates(city: City): Boolean {
    try {
      val postalCode =
          cityService.getPostalCodesByCity(city.id).firstOrNull()
              ?: run {
                logger.error("No postal code found for city: ${city.name}")
                return false
              }
      val coordinates =
          openMeteoClient.getCoordinates(postalCode.code, city.name, city.country.code)
      if (coordinates == null) {
        logger.error("Failed to get coordinates for city: ${city.name}")
        return false
      }
      cityService.updateCityCoordinates(city, coordinates.latitude, coordinates.longitude)
      logger.info("Updated coordinates for city: ${city.name}")
    } catch (e: Exception) {
      logger.error("Failed to update coordinates for city: ${city.name}", e)
      return false
    }
    return true
  }

  suspend fun updateWeatherData(city: City, festival: Festival) {
    try {
      if (city.latitude == null || city.longitude == null) {
        logger.error("City ${city.name} has no coordinates. Skipping weather data update.")
        return
      }
      val days =
          openMeteoClient.getDailyForecast(
              city.latitude!!.toDouble(),
              city.longitude!!.toDouble(),
              festival.startDate,
              festival.endDate,
              OpenMeteoClient.ForecastVariable.entries)
      if (days.isEmpty()) {
        logger.warn("No weather data available for city: ${city.name}")
        return
      }
      festival.minTemperature = days.minOf { it.minTemperature }
      festival.maxTemperature = days.maxOf { it.maxTemperature }
      festival.avgTemperature =
          days.map { (it.maxTemperature + it.minTemperature) / 2 }.average().toFloat()
      festival.avgPrecipitationProbability =
          days.map { it.precipitationProbability }.average().toInt()
      festival.avgPrecipitationSum = days.map { it.precipitationSum }.average().toFloat()
      festival.weatherScore =
          calculateWeatherScore(
              festival.avgPrecipitationProbability!!, festival.avgPrecipitationSum!!)
      weatherService.saveWeatherDataForCity(city.id, days)
      logger.info("Updated weather data for city: ${city.name}")
    } catch (e: Exception) {
      logger.error("Error updating weather data for city: ${city.name}", e)
    }
  }

  fun calculateWeatherScore(avgPrecipitationProbability: Int, avgPrecipitationSum: Float): Float {
    // Precipitation probability score (0-50 points)
    // 0% chance of rain gives 50 points, 100% chance gives 0 points
    val precipProbabilityScore = 50f * (1f - avgPrecipitationProbability / 100f)

    // Precipitation amount score (0-50 points)
    // 0mm of rain gives 50 points, 50mm or more gives 0 points
    val precipAmountScore = max(0f, 50f - avgPrecipitationSum)

    // Sum up the scores
    return precipProbabilityScore + precipAmountScore
  }
}
