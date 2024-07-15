package de.festivalgpt.backend.repository

import de.festivalgpt.backend.model.*
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface PostalCodeRepository : JpaRepository<PostalCode, Long> {
  fun findByCodeAndCity(code: String, city: City): Optional<PostalCode>
  fun findAllByCityId(cityId: Long): List<PostalCode>
}
