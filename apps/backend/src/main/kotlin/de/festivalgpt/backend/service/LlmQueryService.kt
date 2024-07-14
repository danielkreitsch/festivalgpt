package de.festivalgpt.backend.service

import de.festivalgpt.backend.config.AnthropicConfig
import de.festivalgpt.backend.model.Festival
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.anthropic.AnthropicChatModel
import dev.langchain4j.service.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LlmQueryService(
    config: AnthropicConfig,
    private val festivalService: FestivalService,
    private val dynamicQueryService: DynamicQueryService,
) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  /** This assistant is used to interact with the Anthropic chat model. */
  private val assistant: Assistant =
      AiServices.builder(Assistant::class.java)
          .chatLanguageModel(
              AnthropicChatModel.builder().apiKey(config.apiKey).modelName(config.model).build())
          .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
          .build()

  fun findFestivalsByMessage(userMessage: String = ""): List<Festival> {
    var query = assistant.createFestivalQuery("2024-07-15", userMessage)
    println("\nQuery:\n$query\n")
    query = query.replace("\n", " ")
    return findFestivalsBySqlQuery(query)
  }

  fun findFestivalsBySqlQuery(sqlQuery: String): List<Festival> {
    val results = this.dynamicQueryService.executeDynamicQuery(sqlQuery)
    val festivals = results.map { this.festivalService.findFestivalById(it["id"] as Long)!! }
    return festivals
  }
}

internal interface Assistant {
  @SystemMessage(
      """Extract a MySQL query of the user's question to find festivals.
---
Only use the provided tables and columns and ignore all other requirements:
- festival:
  - id: LONG
  - postal_code_id: LONG
  - start_date: DATE
  - end_date: DATE
- postal_code:
  - id: LONG
  - code: VARCHAR
  - city_id: LONG
- city:
  - id: LONG
  - country_code: VARCHAR (default: 'de')
  - name: VARCHAR
---
The current date is: {{currentDate}}
---
Requirements:
- Return only the query string, without any markdown markup surrounding it.
- Start with `SELECT festival.id FROM festival `.
""")
  fun createFestivalQuery(
      @V("currentDate") currentDate: String,
      @UserMessage userMessage: String
  ): String
}
