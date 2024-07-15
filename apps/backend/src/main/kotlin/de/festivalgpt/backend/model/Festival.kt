package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Schema(description = "Festival entity representing a festival.")
data class Festival(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the festival", example = "1")
    val id: Long = 0,
    @Column(name = "name", nullable = false)
    @Schema(description = "Name of the festival", example = "Music Festival")
    val name: String,
    @Column(name = "start_date", nullable = false)
    @Schema(description = "Start date of the festival", example = "2024-07-19")
    val startDate: LocalDate,
    @Column(name = "end_date", nullable = false)
    @Schema(description = "End date of the festival", example = "2024-07-21")
    val endDate: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postal_code_id", nullable = false)
    @Schema(description = "Postal code where the festival is located")
    val postalCode: PostalCode,
    // Redundant data for easier querying
    @Column(name = "city")
    @Schema(description = "City where the festival is located", example = "Berlin")
    var city: String? = null,
    @Column(name = "country_code", length = 2)
    @Schema(description = "ISO 3166-1 alpha-2 country code", example = "DE")
    var countryCode: String? = null,
    @Column(name = "latitude", precision = 10, scale = 8)
    @Schema(description = "Latitude of the festival location", example = "52.52000659999999")
    var latitude: BigDecimal? = null,
    @Column(name = "longitude", precision = 11, scale = 8)
    @Schema(description = "Longitude of the festival location", example = "13.404953999999975")
    var longitude: BigDecimal? = null,
    @Column(name = "min_temperature")
    @Schema(description = "Minimum temperature during the festival in Celsius", example = "15.5")
    var minTemperature: Float? = null,
    @Column(name = "max_temperature")
    @Schema(description = "Maximum temperature during the festival in Celsius", example = "25.5")
    var maxTemperature: Float? = null,
    @Column(name = "avg_temperature")
    @Schema(description = "Average temperature during the festival in Celsius", example = "20.5")
    var avgTemperature: Float? = null,
    @Column(name = "avg_precipitation_probability")
    @Schema(description = "Average precipitation probability during the festival", example = "30")
    var avgPrecipitationProbability: Int? = null,
    @Column(name = "avg_precipitation_sum")
    @Schema(description = "Average precipitation sum during the festival in mm", example = "2.5")
    var avgPrecipitationSum: Float? = null,
    @Column(name = "weather_score")
    @Schema(description = "Weather score for the festival (0-100)", example = "75.5")
    var weatherScore: Float? = null,
    @Column(name = "enabled", nullable = false)
    @Schema(description = "Whether the festival should be considered", example = "true")
    var enabled: Boolean = false,
    @Column(name = "last_updated", nullable = false)
    @Schema(description = "Last update of the festival entity", example = "2024-07-19")
    var lastUpdated: LocalDate = LocalDate.now()
)
