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
    private val anthropicClient: AnthropicClient,
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
    val aiResponse = anthropicClient.sendMessage(request.message)
    val relevantFestivals = emptyList<Festival>()

    return ChatResponse(
        chatId = request.chatId,
        message = aiResponse.content[0].text,
        festivals = relevantFestivals)
  }
}
