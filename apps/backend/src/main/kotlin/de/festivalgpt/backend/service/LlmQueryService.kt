package de.festivalgpt.backend.service

import de.festivalgpt.backend.config.AnthropicConfig
import de.festivalgpt.backend.model.Festival
import dev.langchain4j.model.anthropic.AnthropicChatModel
import dev.langchain4j.service.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
              AnthropicChatModel.builder()
                  .apiKey(config.apiKey)
                  .modelName(config.model)
                  .temperature(0.1)
                  .build())
          .build()

  init {
    findFestivalsByMessage("welche festivals sind in der nähe von nürnberg und haben gutes wetter?")
  }

  fun findFestivalsByMessage(userMessage: String = ""): List<Festival> {
    var sqlQuery =
        assistant.createFestivalQuery(
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), userMessage)
    println("\nQuery:\n$sqlQuery\n")
    sqlQuery = sqlQuery.replace("\n", " ")
    return findFestivalsBySqlQuery(sqlQuery)
  }

  fun findFestivalsBySqlQuery(sqlQuery: String): List<Festival> {
    val results = this.dynamicQueryService.executeDynamicQuery(sqlQuery)
    val festivals = results.map { this.festivalService.findFestivalById(it["id"] as Long)!! }
    return festivals
  }

  fun generateResponse(userMessage: String, data: String): String {
    val response = assistant.generateResponse(userMessage, data)
    println("Data:\n$data\n")
    println("Response:\n$response\n")
    return response
  }
}

internal interface Assistant {
  @SystemMessage("""Find festivals based on the user's message.""")
  fun getFestivals(@UserMessage userMessage: String): String

  @SystemMessage(
      """Read the user's message carefully and extract a MySQL query to find festivals.
---
Only use the provided tables and columns and ignore all unhandled requirements:
- festival:
  - id: LONG
  - name: VARCHAR
  - city: VARCHAR
  - country_code: VARCHAR
  - start_date: DATE
  - end_date: DATE
  - weather_score: FLOAT (from 0.0 to 100.0)
  - latitude: DECIMAL(10,8)
  - longitude: DECIMAL(11,8)
  - avg_precipitation_probability: INT
  - avg_precipitation_sum: FLOAT
  - min_temperature: FLOAT
  - max_temperature: FLOAT
- city:
  - name: VARCHAR
  - latitude: DECIMAL(10,8)
  - longitude: DECIMAL(11,8)
---
The current date is: {{currentDate}}
---
Requirements:
- When querying names, use the `LIKE` operator with a wildcard on both sides.
- When querying by date, you should include all festivals which are running today or in the future up to 14 days.
- Return only the query string, without any markdown markup surrounding it.
- Start with `SELECT festival.id FROM festival `. No other starting point is allowed.
""")
  fun createFestivalQuery(
      @V("currentDate") currentDate: String,
      @UserMessage userMessage: String
  ): String

  @SystemMessage(
      """Generate a response to the user's message like you're a chatbot.
You are providing valuable information about festivals and their weather forecasts.
You have this data:
{{data}}
Example response:
Das Wetter bei Festival X wird sonnig, wohingegen es bei Festival Y regnen wird. Hier sind die Details: ...
      """)
  fun generateResponse(@UserMessage userMessage: String, @V("data") data: String): String
}
