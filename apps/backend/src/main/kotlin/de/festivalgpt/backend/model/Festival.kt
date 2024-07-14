package de.festivalgpt.backend.model

import java.time.LocalDate
import java.util.*

data class Festival(
    val id: UUID,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val location: String,
    val description: String,
    val genre: String,
    val ticketPrice: Double,
    val website: String
)
