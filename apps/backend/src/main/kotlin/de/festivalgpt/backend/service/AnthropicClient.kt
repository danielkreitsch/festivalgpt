package de.festivalgpt.backend.service

import de.festivalgpt.backend.config.AnthropicConfig
import de.festivalgpt.backend.model.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class AnthropicClient(private val restClient: RestClient, private val config: AnthropicConfig) {

  fun sendMessage(userMessage: String): AnthropicResponse {
    val request =
        AnthropicRequest(
            model = "claude-3-sonnet-20240229",
            maxTokens = 1024,
            messages = listOf(AnthropicRequest.Message("user", userMessage)))

    return restClient
        .post()
        .uri("https://api.anthropic.com/v1/messages")
        .header("x-api-key", config.apiKey)
        .header("anthropic-version", "2023-06-01")
        .header("content-type", "application/json")
        .body(request)
        .retrieve()
        .body(AnthropicResponse::class.java)
        ?: throw RuntimeException("Failed to get response from Anthropic API")
  }
}
