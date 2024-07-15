package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema

data class ChatRequest(
    @Schema(description = "User identifier") //
    val userId: String? = null,
    @Schema(description = "Unique identifier for the chat session") //
    val chatId: String? = null,
    @Schema(description = "Message content") //
    val message: String,
    @Schema(description = "User's location") //
    val location: String? = null
)
