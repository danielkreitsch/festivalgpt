package de.festivalgpt.backend.model

// From docs:
// {
//  "content": [
//  {
//    "text": "Hi! My name is Claude.",
//    "type": "text"
//  }
//  ],
//  "id": "msg_013Zva2CMHLNnXjNJJKqJ2EF",
//  "model": "claude-3-5-sonnet-20240620",
//  "role": "assistant",
//  "stop_reason": "end_turn",
//  "stop_sequence": null,
//  "type": "message",
//  "usage": {
//  "input_tokens": 10,
//  "output_tokens": 25
// }
// }

data class AnthropicResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<Content>,
    val model: String,
    val stopReason: String?,
    val stopSequence: String?
) {
  data class Content(val type: String, val text: String)
}
