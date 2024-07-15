package de.festivalgpt.backend.repository

import de.festivalgpt.backend.model.WeatherForecast
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WeatherForecastRepository : JpaRepository<WeatherForecast, Long> {
  fun findByCityIdAndDateBetween(
      cityId: Long,
      startDate: LocalDate,
      endDate: LocalDate
  ): List<WeatherForecast>
  fun deleteByCityIdAndDateBefore(cityId: Long, date: LocalDate)
}
