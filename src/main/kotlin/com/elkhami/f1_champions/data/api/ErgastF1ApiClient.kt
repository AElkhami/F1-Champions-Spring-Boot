package com.elkhami.f1_champions.data.api

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ErgastF1ApiClient (private val webClientBuilder: WebClient.Builder) : F1ApiClient {
    private val baseUrl = "https://ergast.com/api/f1"

    override fun fetchChampions(): List<Champion> {
        val response = webClientBuilder.build()
            .get()
            .uri("$baseUrl/driverStandings/1.json?limit=1000")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return ErgastParser.parseChampions(response)
    }

    override fun fetchRaceWinners(season: String): List<RaceResult> {
        val response = webClientBuilder.build()
            .get()
            .uri("$baseUrl/$season/results/1.json")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return ErgastParser.parseRaceWinners(season, response)
    }
}