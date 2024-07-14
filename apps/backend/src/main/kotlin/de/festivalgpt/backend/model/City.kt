package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Schema(description = "City entity representing a city.")
data class City(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the city", example = "1")
    val id: Long = 0,
    @Column(name = "name", nullable = false)
    @Schema(description = "Name of the city", example = "New York")
    val name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", nullable = false)
    @Schema(description = "Country the city belongs to")
    val country: Country
)
