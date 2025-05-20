package com.elkhami.f1_champions.data.api

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult

interface F1ApiClient {
    fun fetchChampions(): List<Champion>
    fun fetchRaceWinners(season: String): List<RaceResult>
}