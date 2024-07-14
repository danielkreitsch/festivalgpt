package de.festivalgpt.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class WebConfig {

  @Bean
  fun restClient(): RestClient {
    return RestClient.builder().build()
  }
}
