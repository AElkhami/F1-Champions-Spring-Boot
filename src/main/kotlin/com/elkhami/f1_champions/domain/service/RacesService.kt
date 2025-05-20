package com.elkhami.f1_champions.domain.service

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult

interface RacesService {
    fun syncData()
    fun getChampions(): List<Champion>
    fun getRaceWinners(season: String): List<RaceResult>
}