package de.festivalgpt.backend.service

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.repository.*
import jakarta.transaction.Transactional
import java.math.BigDecimal
import org.springframework.stereotype.Service

@Service
class CityService(
    private val cityRepository: CityRepository,
    private val postalCodeRepository: PostalCodeRepository
) {
  /** Get all cities. */
  fun getAllCities(): List<City> = cityRepository.findAll()

  /** Get a city by its ID. */
  fun getCityById(id: Long): City =
      cityRepository.findById(id).orElseThrow { IllegalArgumentException("City not found") }

  /** Get all enabled cities. */
  fun getEnabledCities(): List<City> {
    return getAllCities().filter { it.enabled }
  }

  /** Get all disabled cities. */
  fun getDisabledCities(): List<City> {
    return getAllCities().filter { !it.enabled }
  }

  fun getPostalCodesByCity(cityId: Long): List<PostalCode> {
    return postalCodeRepository.findAllByCityId(cityId)
  }

  @Transactional
  fun updateCityCoordinates(city: City, latitude: BigDecimal, longitude: BigDecimal): City {
    city.latitude = latitude
    city.longitude = longitude
    city.enabled = true
    return cityRepository.save(city)
  }
}
