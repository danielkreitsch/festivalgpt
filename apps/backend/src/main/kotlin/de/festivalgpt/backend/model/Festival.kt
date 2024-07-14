package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

@Schema(description = "Festival information (WIP)")
data class Festival(
    @Schema(description = "Unique identifier for the festival") val id: UUID,
    @Schema(description = "Name of the festival") val name: String,
    @Schema(description = "Start date of the festival") val startDate: LocalDate,
    @Schema(description = "End date of the festival") val endDate: LocalDate,
    @Schema(description = "Location of the festival") val location: String,
    @Schema(description = "Description of the festival") val description: String,
    @Schema(description = "Genre of the festival") val genre: String,
    @Schema(description = "Price of the festival ticket") val ticketPrice: Double,
    @Schema(description = "Website of the festival") val website: String
)
