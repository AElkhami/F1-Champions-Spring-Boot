package com.elkhami.f1_champions.seasondetails.intrastructure.api

import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SeasonDetailsClient (private val webClientBuilder: WebClient.Builder) {
    private val baseUrl = "https://ergast.com/api/f1"

    fun fetchSeasonDetails(season: String): List<SeasonDetail> {
        val response = webClientBuilder.build()
            .get()
            .uri("$baseUrl/$season/results/1.json")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return SeasonDetailsParser.parseSeasonDetails(season, response)
    }
}