package de.festivalgpt.backend.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
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
    val postalCode: PostalCode
)
