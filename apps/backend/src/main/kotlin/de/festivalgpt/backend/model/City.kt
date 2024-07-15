package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.math.BigDecimal

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
    val country: Country,
    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    @Schema(description = "Latitude of the city", example = "40.7128")
    var latitude: BigDecimal,
    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    @Schema(description = "Longitude of the city", example = "-74.0060")
    var longitude: BigDecimal,
    @Column(name = "enabled", nullable = false)
    @Schema(description = "Whether the city should be considered", example = "true")
    var enabled: Boolean
)
