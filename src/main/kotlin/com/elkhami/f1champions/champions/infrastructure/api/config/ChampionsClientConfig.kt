package com.elkhami.f1champions.champions.infrastructure.api.config

import com.elkhami.f1champions.champions.infrastructure.api.ChampionsClient
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ChampionsClientConfig {

    @Bean
    fun championsClient(
        @Value("\${f1.api.base-url}") baseUrl: String,
        webClientBuilder: WebClient.Builder,
        circuitBreakerRegistry: CircuitBreakerRegistry,
        rateLimiterRegistry: RateLimiterRegistry,
        retryRegistry: RetryRegistry
    ): ChampionsClient {
        return ChampionsClient(
            webClientBuilder = webClientBuilder,
            baseUrl = baseUrl,
            circuitBreaker = circuitBreakerRegistry.circuitBreaker("champions-client"),
            rateLimiter = rateLimiterRegistry.rateLimiter("champions-rate-limiter"),
            retry = retryRegistry.retry("champions-retry")
        )
    }
}
