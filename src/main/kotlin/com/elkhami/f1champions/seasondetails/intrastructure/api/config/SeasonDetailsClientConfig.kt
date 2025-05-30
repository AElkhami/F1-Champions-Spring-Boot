package com.elkhami.f1champions.seasondetails.intrastructure.api.config

import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SeasonDetailsClientConfig {
    @Bean
    fun seasonDetailsClient(
        @Value("\${f1.api.base-url}") baseUrl: String,
        webClientBuilder: WebClient.Builder,
        retryRegistry: RetryRegistry,
    ): SeasonDetailsClient {
        return SeasonDetailsClient(
            webClientBuilder = webClientBuilder,
            baseUrl = baseUrl,
            retry = retryRegistry.retry("season-details-retry"),
        )
    }
}
