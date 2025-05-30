package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.utils.loggerWithPrefix
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ChampionsClient(
    private val webClientBuilder: WebClient.Builder,
    private val baseUrl: String,
    private val circuitBreaker: CircuitBreaker,
    private val rateLimiter: RateLimiter,
    private val retry: Retry
) {
    private val logger = loggerWithPrefix()

    suspend fun fetchChampion(year: Int): Champion? {
        return runCatching {
            retry.executeSuspendFunction {
                rateLimiter.executeSuspendFunction {
                    circuitBreaker.executeSuspendFunction {
                        fetchFromApi(year)
                            ?.also { logger.info("✅ Got champion for $year") }
                    }
                }
            }
        }.getOrElse {
            logger.info("⚠️ Failed to fetch champion for $year: ${it.message}")
            null
        }
    }

    internal suspend fun fetchFromApi(year: Int): Champion? {
        val response = webClientBuilder.build()
            .get()
            .uri("$baseUrl/$year/driverStandings/1.json")
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()

        return ChampionParser.parseChampions(response).firstOrNull()
    }
}
