package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ChatResponse(
  @Schema(description = "Unique identifier for the chat session") //
  val chatId: String,
  @Schema(description = "Response message") //
  val message: String,
  @Schema(description = "List of relevant festivals") //
  val festivals: List<Festival> = emptyList()
)
