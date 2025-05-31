package com.elkhami.f1champions.seasondetails.intrastructure.api

import com.elkhami.f1champions.core.logger.loggerWithPrefix
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.Retry
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SeasonDetailsClient(
    private val webClientBuilder: WebClient.Builder,
    private val baseUrl: String,
    private val retry: Retry,
) {
    private val logger = loggerWithPrefix()

    suspend fun fetchSeasonDetails(season: String): List<SeasonDetail> {
        return runCatching {
            retry.executeSuspendFunction {
                fetchFromApi(season)
                    .also { logger.info("✅ Got ${it.size} races for $season") }
            }
        }.getOrElse {
            logger.warn("⚠️ Failed to fetch details for $season: ${it.message}")
            emptyList()
        }
    }

    internal suspend fun fetchFromApi(season: String): List<SeasonDetail> {
        val response =
            webClientBuilder.build()
                .get()
                .uri("$baseUrl/$season/results/1.json")
                .retrieve()
                .bodyToMono(String::class.java)
                .awaitSingle()

        return SeasonDetailsParser.parseSeasonDetails(season, response)
    }
}
