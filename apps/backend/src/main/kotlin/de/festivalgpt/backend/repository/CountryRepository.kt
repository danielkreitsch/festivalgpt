package de.festivalgpt.backend.repository

import de.festivalgpt.backend.model.Country
import org.springframework.data.jpa.repository.JpaRepository

interface CountryRepository : JpaRepository<Country, String>
