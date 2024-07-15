package de.festivalgpt.backend.controller

import de.festivalgpt.backend.model.ImportFestivalsRequest
import de.festivalgpt.backend.service.FestivalService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(private val festivalService: FestivalService) {
  @PostMapping("/import-festivals")
  fun importFestivals(@RequestBody request: ImportFestivalsRequest) {
    festivalService.importFestivalsFromCsv(
        System.getProperty("user.home") + "/.festivalgpt/festivals.csv",
        request.index,
        request.count)
  }
}
