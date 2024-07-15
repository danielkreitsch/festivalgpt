package de.festivalgpt.backend.controller

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.service.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val llmQueryService: LlmQueryService,
    private val weatherService: WeatherService
) {
  @PostMapping("/message")
  @Operation(
      summary = "Send a message",
      description = "Send a message and get a response along with relevant festivals")
  @ApiResponse(
      responseCode = "200",
      description = "Successful response",
      content =
          [
              Content(
                  mediaType = "application/json",
                  schema = Schema(implementation = ChatResponse::class))])
  fun sendMessage(@RequestBody request: ChatRequest): ChatResponse {
    val festivals = llmQueryService.findFestivalsByMessage(request.message)
    val aggregatedFestivals =
        festivals.map { festival ->
          val weatherForecast =
              weatherService.getWeatherForecast(
                  festival.postalCode.city.id, festival.startDate, festival.endDate)
          ChatResponse.Festival(
              id = festival.id,
              name = festival.name,
              city = festival.postalCode.city.name,
              startDate = festival.startDate,
              endDate = festival.endDate,
              weatherForecast =
                  weatherForecast.map { weather ->
                    ChatResponse.DailyWeather(
                        date = weather.date,
                        maxTemperature = weather.maxTemperature,
                        minTemperature = weather.minTemperature,
                        precipitationSum = weather.precipitationSum,
                        precipitationProbability = weather.precipitationProbability)
                  })
        }

    return ChatResponse(
        chatId = "1",
        message = "Folgende Festivals könnten für dich interessant sein: {{festivals}}",
        festivals = aggregatedFestivals)
  }
}
