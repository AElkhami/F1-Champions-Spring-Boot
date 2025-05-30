package com.elkhami.f1_champions.seasondetails.intrastructure.api

import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1_champions.utils.loggerWithPrefix
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class SeasonDetailsClient(
    private val webClientBuilder: WebClient.Builder,
    registry: CircuitBreakerRegistry
) {
    private val logger = loggerWithPrefix()

    @Value("\${f1.api.base-url}")
    private lateinit var baseUrl: String

    private val circuitBreaker = registry.circuitBreaker("seasonDetailsApi")

    suspend fun fetchSeasonDetails(season: String): List<SeasonDetail> {
        return runCatching {
            circuitBreaker.executeSuspendFunction {
                val response = webClientBuilder.build()
                    .get()
                    .uri("$baseUrl/$season/results/1.json")
                    .retrieve()
                    .awaitBody<String>()

                SeasonDetailsParser.parseSeasonDetails(season, response)
            }
        }.getOrElse {
            logger.info("⚠️ Failed to fetch details for $season: ${it.message}")
            emptyList()
        }
    }
}