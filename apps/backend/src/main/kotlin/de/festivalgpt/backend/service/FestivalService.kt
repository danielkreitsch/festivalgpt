package de.festivalgpt.backend.service

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.repository.*
import java.io.File
import java.time.LocalDate
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FestivalService(
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val cityRepository: CityRepository,
    @Autowired private val postalCodeRepository: PostalCodeRepository,
    @Autowired private val festivalRepository: FestivalRepository
) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  fun findAllFestivals(): List<Festival> {
    return festivalRepository.findAll()
  }

  fun findFestivalById(id: Long): Festival? {
    return festivalRepository.findById(id).orElse(null)
  }

  fun findFestivalsByNameContaining(query: String): List<FestivalAutocompleteResponse> {
    return findAllFestivals()
        .filter { it.name.contains(query, ignoreCase = true) }
        .map { FestivalAutocompleteResponse(it.id, it.name) }
        .take(5)
  }

  fun importFestivalsFromCsv(csvFile: String, index: Int, count: Int) {
    val lines = File(csvFile).readLines().drop(1)
    val subList = lines.drop(index).take(count)

    subList.forEach { line ->
      if (line.count { it == ',' } != 5) {
        logger.error("Invalid CSV line: $line")
        return@forEach
      }
      val parts = line.split(",")
      val startDate = LocalDate.parse(parts[0].trim())
      val endDate = runCatching { LocalDate.parse(parts[1].trim()) }.getOrElse { startDate }
      val name = parts[2].trim()
      val countryCode = parts[3].trim()
      val postalCodeStr = parts[4].trim()
      val cityName = parts[5].trim()
      val country =
          countryRepository.findById(countryCode).orElseGet {
            val newCountry =
                Country(
                    code = countryCode, name = countryCode) // Map code to country name if needed
            countryRepository.save(newCountry)
          }
      val city =
          cityRepository.findByNameAndCountry(cityName, country).orElseGet {
            val newCity =
                City(
                    name = cityName,
                    country = country,
                    latitude = null,
                    longitude = null,
                    enabled = false)
            cityRepository.save(newCity)
          }
      val postalCode =
          postalCodeRepository.findByCodeAndCity(postalCodeStr, city).orElseGet {
            val newPostalCode = PostalCode(code = postalCodeStr, city = city)
            postalCodeRepository.save(newPostalCode)
          }
      val festival =
          Festival(name = name, startDate = startDate, endDate = endDate, postalCode = postalCode)
      festivalRepository.save(festival)

      logger.info("Loaded festival: $festival")
    }
  }
}
