package com.elkhami.f1_champions.champions.controller

import com.elkhami.f1_champions.champions.domain.service.ChampionsService
import com.elkhami.f1_champions.champions.domain.model.Champion
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/f1")
class ChampionsController(private val service: ChampionsService) {

    @GetMapping("/champions")
    fun getChampions(): List<Champion> = service.getChampions()
}