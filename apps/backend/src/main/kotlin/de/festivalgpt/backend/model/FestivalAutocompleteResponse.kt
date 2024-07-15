package de.festivalgpt.backend.model

data class FestivalAutocompleteResponse(
    val id: Long,
    val name: String,
    val city: String,
    val description: String
)
