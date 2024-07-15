package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class ChatResponse(
    @Schema(description = "Unique identifier for the chat session") val chatId: String,
    @Schema(description = "Response message") val message: String,
    @Schema(description = "List of relevant festivals") val festivals: List<Festival> = emptyList()
) {
  data class Festival(
      @Schema(description = "Unique identifier of the festival", example = "21") val id: Long,
      @Schema(description = "Name of the festival", example = "Jazzopen") val name: String,
      @Schema(description = "Name of the city where the festival is located", example = "Stuttgart")
      val city: String,
      @Schema(description = "Start date of the festival", example = "2024-07-18")
      val startDate: LocalDate,
      @Schema(description = "End date of the festival", example = "2024-07-29")
      val endDate: LocalDate,
      @Schema(description = "Weather forecast for each day of the festival")
      val weatherForecast: List<DailyWeather>
  )

  data class DailyWeather(
      @Schema(description = "Date of the weather forecast", example = "2024-07-18")
      val date: LocalDate,
      @Schema(description = "Maximum temperature in Celsius", example = "28.7")
      val maxTemperature: Float,
      @Schema(description = "Minimum temperature in Celsius", example = "15.9")
      val minTemperature: Float,
      @Schema(description = "Total precipitation in millimeters", example = "5.4")
      val precipitationSum: Float,
      @Schema(description = "Probability of precipitation as a percentage", example = "29")
      val precipitationProbability: Int
  )
}
