package de.festivalgpt.backend.controller

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatbot")
class ChatbotController(
    private val anthropicClient: AnthropicClient,
    private val festivalService: FestivalService,
    private val chatService: ChatService
) {

  @PostMapping("/query")
  fun query(@RequestBody request: ChatbotRequest): ChatbotResponse {
    // Store or update chat information
    chatService.storeOrUpdateChat(request.chatId, request.userId, request.location)

    val aiResponse = anthropicClient.sendMessage(request.message)
    val relevantFestivals = festivalService.findRelevantFestivals(request.message, request.chatId)

    return ChatbotResponse(
        chatId = request.chatId,
        message = aiResponse.content[0].text,
        festivals = relevantFestivals)
  }
}
