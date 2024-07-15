package de.festivalgpt.backend.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal
import java.time.LocalDate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class OpenMeteoClient(private val objectMapper: ObjectMapper) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val restClient = RestClient.create()
  private val forecastBaseUrl = "https://api.open-meteo.com/v1/forecast"
  private val geocodingBaseUrl = "https://geocoding-api.open-meteo.com/v1/search"

  fun getDailyForecast(
      latitude: Double,
      longitude: Double,
      startDate: LocalDate,
      endDate: LocalDate,
      variables: List<ForecastVariable>
  ): List<DailyWeatherData> {
    val today = LocalDate.now()
    val maxEndDate = today.plusDays(15)

    val validStartDate = startDate.coerceAtLeast(today)
    val validEndDate = endDate.coerceAtMost(maxEndDate)

    if (validStartDate.isAfter(validEndDate)) {
      return emptyList()
    }

    val url =
        UriComponentsBuilder.fromHttpUrl(forecastBaseUrl)
            .queryParam("latitude", latitude)
            .queryParam("longitude", longitude)
            .queryParam("daily", variables.joinToString(",") { it.apiName })
            .queryParam("timezone", "auto")
            .queryParam("start_date", validStartDate)
            .queryParam("end_date", validEndDate)
            .toUriString()

    val response = restClient.get().uri(url).retrieve().body(String::class.java)

    val forecastResponse = objectMapper.readValue<ForecastResponse>(response!!)
    val fullForecastList = forecastResponse.daily.toDailyWeatherDataList()

    // Filter the results to match the original date range if it was adjusted
    return fullForecastList.filter { it.date in startDate..endDate }
  }

  fun getCoordinates(postalCode: String, cityName: String, countryCode: String): Coordinates? {
    val modifiedCityName =
        cityName.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss")

    fun fetchCoordinates(query: String): Coordinates? {
      val url =
          UriComponentsBuilder.fromHttpUrl(geocodingBaseUrl)
              .queryParam("name", query)
              .queryParam("count", 1)
              .queryParam("language", "de")
              .queryParam("format", "json")
              .toUriString()

      val response =
          try {
            restClient.get().uri(url).retrieve().body(String::class.java)
          } catch (e: Exception) {
            logger.error("Error fetching coordinates for query: $query", e)
            return null
          }

      val geocodingResponse = objectMapper.readValue<GeocodingResponse>(response!!)
      return geocodingResponse.results?.firstOrNull()?.let { result ->
        if (query == postalCode && result.country_code.equals(countryCode, ignoreCase = true)) {
          Coordinates(BigDecimal(result.latitude), BigDecimal(result.longitude))
        } else if (query != postalCode) {
          Coordinates(BigDecimal(result.latitude), BigDecimal(result.longitude))
        } else null
      }
    }

    // First, try with the modified city name
    return fetchCoordinates(modifiedCityName) ?: fetchCoordinates(postalCode)
  }

  data class Coordinates(val latitude: BigDecimal, val longitude: BigDecimal)

  @JsonIgnoreProperties(ignoreUnknown = true) data class ForecastResponse(val daily: DailyData)

  @JsonIgnoreProperties(ignoreUnknown = true)
  data class DailyData(
      val time: List<String>,
      val temperature_2m_max: List<Float>,
      val temperature_2m_min: List<Float>,
      val precipitation_sum: List<Float>,
      val precipitation_probability_max: List<Int>
  ) {
    fun toDailyWeatherDataList(): List<DailyWeatherData> {
      return time.indices.map { i ->
        DailyWeatherData(
            date = LocalDate.parse(time[i]),
            maxTemperature = temperature_2m_max[i],
            minTemperature = temperature_2m_min[i],
            precipitationSum = precipitation_sum[i],
            precipitationProbability = precipitation_probability_max[i])
      }
    }
  }

  data class DailyWeatherData(
      val date: LocalDate,
      val maxTemperature: Float,
      val minTemperature: Float,
      val precipitationSum: Float,
      val precipitationProbability: Int
  )

  enum class ForecastVariable(val apiName: String) {
    TEMPERATURE_2M_MAX("temperature_2m_max"),
    TEMPERATURE_2M_MIN("temperature_2m_min"),
    PRECIPITATION_SUM("precipitation_sum"),
    PRECIPITATION_PROBABILITY_MAX("precipitation_probability_max")
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  data class GeocodingResponse(val results: List<GeocodingResult>?)

  @JsonIgnoreProperties(ignoreUnknown = true)
  data class GeocodingResult(
      val name: String,
      val latitude: Double,
      val longitude: Double,
      val country_code: String
  )
}
