package com.elkhami.f1champions.champions.infrastructure.api

import com.elkhami.f1champions.champions.domain.model.Champion
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ChampionsClientTest {
    private val webClientBuilder = mockk<WebClient.Builder>(relaxed = true)
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)

    private lateinit var client: ChampionsClient

    @BeforeTest
    fun setup() {
        every { webClientBuilder.build() } returns webClient
        every { webClient.get() } returns uriSpec
        every { uriSpec.uri(any<String>()) } returns headersSpec
        every { headersSpec.retrieve() } returns responseSpec

        client =
            ChampionsClient(
                webClientBuilder = webClientBuilder,
                baseUrl = "http://mock-api.com",
                circuitBreaker = mockk(relaxed = true),
                rateLimiter = mockk(relaxed = true),
                retry = mockk(relaxed = true),
            )
    }

    @Test
    fun `fetchFromApi returns parsed champion`() =
        runTest {
            val json = """{ "mock": "response" }"""

            every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

            mockkObject(ChampionParser)
            every { ChampionParser.parseChampions(json) } returns
                listOf(
                    Champion("2020", "hamilton", "Lewis Hamilton", "Mercedes"),
                )

            val result = client.fetchFromApi(2020)

            assertNotNull(result)
            assertEquals("2020", result.season)
        }
}
