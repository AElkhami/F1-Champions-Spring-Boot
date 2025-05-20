package com.elkhami.f1_champions.controllers

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import com.elkhami.f1_champions.domain.service.RacesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/f1")
class F1Controller(private val service: RacesService) {

    @GetMapping("/champions")
    fun getChampions(): List<Champion> = service.getChampions()

    @GetMapping("/champions/{season}")
    fun getWinners(@PathVariable season: String): List<RaceResult> =
        service.getRaceWinners(season)
}
