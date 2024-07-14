package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Schema(description = "PostalCode entity representing a postal code associated with a city.")
data class PostalCode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the postal code", example = "1")
    val id: Long = 0,
    @Column(name = "code", nullable = false)
    @Schema(description = "Postal code", example = "10001")
    val code: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @Schema(description = "City the postal code belongs to")
    val city: City
)
