package com.elkhami.f1_champions.champions.infrastructure.api

import com.elkhami.f1_champions.champions.domain.model.Champion
import com.elkhami.f1_champions.utils.loggerWithPrefix
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class ChampionsClient(
    private val webClientBuilder: WebClient.Builder,
    circuitBreakerRegistry: CircuitBreakerRegistry,
    rateLimiterRegistry: RateLimiterRegistry,
    retryRegistry: RetryRegistry
) {
    private val logger = loggerWithPrefix()

    @Value("\${f1.api.base-url}")
    private lateinit var baseUrl: String

    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker("champions-client")
    private val rateLimiter = rateLimiterRegistry.rateLimiter("champions-rate-limiter")
    private val retry = retryRegistry.retry("champions-retry") // <- New

    suspend fun fetchChampion(year: Int): Champion? {
        val client = webClientBuilder.build()

        return runCatching {
            retry.executeSuspendFunction {
                rateLimiter.executeSuspendFunction {
                    circuitBreaker.executeSuspendFunction {
                        val response = client.get()
                            .uri("$baseUrl/$year/driverStandings/1.json")
                            .retrieve()
                            .awaitBody<String>()

                        ChampionParser.parseChampions(response).firstOrNull()
                            ?.also { logger.info("✅ Got champion for $year") }
                    }
                }
            }
        }.getOrElse {
            logger.info("⚠️ Failed to fetch champion for $year: ${it.message}")
            null
        }
    }
}
