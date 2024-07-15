package de.festivalgpt.backend.controller

import de.festivalgpt.backend.model.*
import de.festivalgpt.backend.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/festivals")
class FestivalController(private val festivalService: FestivalService) {
  @GetMapping("/autocomplete")
  fun autocompleteFestivals(@RequestParam query: String): List<FestivalAutocompleteResponse> {
    if (query.length < 3) return emptyList()
    return festivalService.findFestivalsByNameContaining(query)
  }
}
