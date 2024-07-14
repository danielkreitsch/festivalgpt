package de.festivalgpt.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "anthropic")
class AnthropicConfig {
  lateinit var apiKey: String
  lateinit var apiUrl: String
}
