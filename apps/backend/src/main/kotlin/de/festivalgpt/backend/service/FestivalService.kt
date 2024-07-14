package de.festivalgpt.backend.service

import de.festivalgpt.backend.model.Festival
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FestivalService(private val chatService: ChatService) {

  // TODO: Implement this method
  fun findRelevantFestivals(message: String, chatId: UUID): List<Festival> {
    val chatInfo = chatService.getChatInfo(chatId)
    // Use chatInfo.location if available
    // Implement logic to find relevant festivals based on the message and location
    // This is a placeholder implementation
    return emptyList()
  }
}
