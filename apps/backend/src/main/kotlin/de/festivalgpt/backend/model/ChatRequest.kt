package de.festivalgpt.backend.model

import java.util.*

data class ChatRequest(
    val userId: UUID,
    val chatId: UUID,
    val message: String,
    val location: String? = null
)
