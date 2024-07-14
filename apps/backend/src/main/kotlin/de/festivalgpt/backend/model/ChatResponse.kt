package de.festivalgpt.backend.model

import java.util.*

data class ChatResponse(
    val chatId: UUID,
    val message: String,
    val festivals: List<Festival> = emptyList()
)
