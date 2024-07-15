package de.festivalgpt.backend.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class WeatherForecast(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    val city: City,
    @Column(nullable = false) val date: LocalDate,
    @Column(nullable = false) var maxTemperature: Float,
    @Column(nullable = false) var minTemperature: Float,
    @Column(nullable = false) var precipitationSum: Float,
    @Column(nullable = false) var precipitationProbability: Int,
    @Column(nullable = false) var lastUpdated: LocalDate = LocalDate.now()
)
