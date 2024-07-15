package de.festivalgpt.backend.service

import de.festivalgpt.backend.model.WeatherForecast
import de.festivalgpt.backend.repository.*
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WeatherService(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val cityRepository: CityRepository
) {
  @Transactional
  fun saveWeatherDataForCity(cityId: Long, weatherData: List<OpenMeteoClient.DailyWeatherData>) {
    val city =
        cityRepository.findById(cityId).orElseThrow { IllegalArgumentException("City not found") }

    // Delete old forecasts
    weatherForecastRepository.deleteByCityIdAndDateBefore(cityId, LocalDate.now())

    // Fetch existing forecasts for the date range
    val startDate = weatherData.minOf { it.date }
    val endDate = weatherData.maxOf { it.date }
    val existingForecasts =
        weatherForecastRepository
            .findByCityIdAndDateBetween(cityId, startDate, endDate)
            .associateBy { it.date }

    // Prepare forecasts to save or update
    val forecastsToSave =
        weatherData.map { dailyData ->
          existingForecasts[dailyData.date]?.apply {
            // Update existing forecast
            maxTemperature = dailyData.maxTemperature
            minTemperature = dailyData.minTemperature
            precipitationSum = dailyData.precipitationSum
            precipitationProbability = dailyData.precipitationProbability
            lastUpdated = LocalDate.now()
          }
              ?: WeatherForecast(
                  // Create new forecast
                  city = city,
                  date = dailyData.date,
                  maxTemperature = dailyData.maxTemperature,
                  minTemperature = dailyData.minTemperature,
                  precipitationSum = dailyData.precipitationSum,
                  precipitationProbability = dailyData.precipitationProbability,
                  lastUpdated = LocalDate.now())
        }

    // Save or update forecasts
    weatherForecastRepository.saveAll(forecastsToSave)
  }

  fun getWeatherForecast(
      cityId: Long,
      startDate: LocalDate,
      endDate: LocalDate
  ): List<WeatherForecast> {
    return weatherForecastRepository.findByCityIdAndDateBetween(cityId, startDate, endDate)
  }
}
