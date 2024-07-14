package de.festivalgpt.backend.repository

import de.festivalgpt.backend.model.*
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Long> {
  fun findByNameAndCountry(name: String, country: Country): Optional<City>
}
