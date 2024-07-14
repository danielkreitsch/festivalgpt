package de.festivalgpt.backend.service

import java.util.*
import org.springframework.stereotype.Service

@Service
class ChatService {
  // TODO: Store this information in a database
  private val chatInfo = mutableMapOf<UUID, ChatInfo>()

  /** Store or update the chat info for the given chat ID. */
  fun storeOrUpdateChat(chatId: UUID, userId: UUID, location: String?) {
    chatInfo[chatId] = ChatInfo(userId, location ?: chatInfo[chatId]?.location)
  }

  fun getChatInfo(chatId: UUID): ChatInfo? = chatInfo[chatId]
}

data class ChatInfo(val userId: UUID, val location: String?)
