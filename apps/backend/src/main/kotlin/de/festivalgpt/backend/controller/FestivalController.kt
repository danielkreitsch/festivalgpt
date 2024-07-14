package de.festivalgpt.backend.controller

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.service.FestivalService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/festivals")
class FestivalController(private val festivalService: FestivalService) {
  @GetMapping("/autocomplete")
  fun autocompleteFestivals(@RequestParam query: String): List<FestivalAutocompleteResponse> {
    return festivalService.findFestivalsByNameContaining(query)
  }

  @PostMapping("/import")
  fun importFestivals(@RequestBody request: ImportFestivalsRequest) {
    festivalService.importFestivalsFromCsv(
        System.getProperty("user.home") + "/.festivalgpt/festivals.csv",
        request.index,
        request.count)
  }
}