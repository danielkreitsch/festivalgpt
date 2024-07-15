package de.festivalgpt.backend.scheduler

import de.festivalgpt.backend.model.City
import de.festivalgpt.backend.service.*
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CityUpdateScheduler(
    private val cityService: CityService,
    private val weatherService: WeatherService,
    private val openMeteoClient: OpenMeteoClient
) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val isRunning = AtomicBoolean(false)

  @Scheduled(fixedRate = 20000)
  fun updateCities() = runBlocking {
    if (isRunning.compareAndSet(false, true)) {
      try {
        val citiesToUpdate = cityService.getDisabledCities().shuffled().take(1)
        if (citiesToUpdate.isEmpty()) {
          logger.info("No more cities to update.")
          return@runBlocking
        }

        citiesToUpdate.forEach { city ->
          if (updateCoordinates(city)) {
            updateWeatherData(city)
          }
        }
      } finally {
        isRunning.set(false)
      }
    } else {
      logger.info("Previous update task is still running. Skipping this run.")
    }
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

  suspend fun updateWeatherData(city: City) {
    try {
      val weatherData = fetchWeatherData(city.latitude.toDouble(), city.longitude.toDouble())
      weatherService.saveWeatherData(city.id, weatherData)
      logger.info("Updated weather data for city: ${city.name}")
    } catch (e: Exception) {
      logger.error("Error updating weather data for city: ${city.name}", e)
    }
  }

  private suspend fun fetchWeatherData(
      latitude: Double,
      longitude: Double
  ): List<OpenMeteoClient.DailyWeatherData> {
    val startDate = LocalDate.now()
    val days = 7 // Fetch 7 days of forecast

    return openMeteoClient.getDailyForecast(
        latitude = latitude,
        longitude = longitude,
        startDate = startDate,
        days = days,
        variables = OpenMeteoClient.ForecastVariable.values().toList())
  }
}
