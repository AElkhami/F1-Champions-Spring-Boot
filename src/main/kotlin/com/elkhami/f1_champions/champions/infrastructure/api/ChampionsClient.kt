package com.elkhami.f1_champions.champions.infrastructure.api

import com.elkhami.f1_champions.champions.domain.model.Champion
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ChampionsClient (private val webClientBuilder: WebClient.Builder) {
    private val baseUrl = "https://ergast.com/api/f1"

    fun fetchChampions(): List<Champion> {
        val response = webClientBuilder.build()
            .get()
            .uri("$baseUrl/driverStandings/1.json?limit=1000")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return ChampionParser.parseChampions(response)
    }
}