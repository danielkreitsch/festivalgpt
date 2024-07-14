package de.festivalgpt.backend.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AnthropicRequest(
    val model: String,
    @JsonProperty("max_tokens") val maxTokens: Int,
    val messages: List<Message>
) {
  data class Message(val role: String, val content: String)
}
