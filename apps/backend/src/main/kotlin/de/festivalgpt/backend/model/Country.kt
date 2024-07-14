package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Schema(description = "Country entity representing a country.")
data class Country(
    @Id
    @Column(name = "code", nullable = false)
    @Schema(description = "ISO country code", example = "US")
    val code: String,
    @Column(name = "name", nullable = false)
    @Schema(description = "Name of the country", example = "United States")
    val name: String
)
