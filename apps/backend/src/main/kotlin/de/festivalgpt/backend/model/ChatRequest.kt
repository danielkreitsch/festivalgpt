package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ChatRequest(
    @Schema(description = "User identifier") val userId: UUID,
    @Schema(description = "Unique identifier for the chat session") val chatId: UUID,
    @Schema(description = "Message content") val message: String,
    @Schema(description = "User's location") val location: String? = null
)
