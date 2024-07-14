package de.festivalgpt.backend.repository

import de.festivalgpt.backend.model.Festival
import org.springframework.data.jpa.repository.*

interface FestivalRepository : JpaRepository<Festival, Long>
