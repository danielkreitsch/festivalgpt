package de.festivalgpt.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.*

@Configuration
class OpenApiConfig {

  @Bean
  fun customOpenAPI(): OpenAPI {
    return OpenAPI()
        .info(
            Info()
                .title("Chat API")
                .version("1.0")
                .description("API for interacting with the chat service"))
  }
}
