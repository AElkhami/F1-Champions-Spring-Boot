package com.elkhami.f1champions.seasondetails.infrastructure.api

import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsParser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class SeasonDetailsClientTest {

    private val webClientBuilder = mockk<WebClient.Builder>(relaxed = true)
    private val webClient = mockk<WebClient>(relaxed = true)
    private val uriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>(relaxed = true)
    private val headersSpec = mockk<WebClient.RequestHeadersSpec<*>>(relaxed = true)
    private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)

    private lateinit var client: SeasonDetailsClient

    @BeforeTest
    fun setup() {
        every { webClientBuilder.build() } returns webClient
        every { webClient.get() } returns uriSpec
        every { uriSpec.uri(any<String>()) } returns headersSpec
        every { headersSpec.retrieve() } returns responseSpec

        client = SeasonDetailsClient(
            webClientBuilder = webClientBuilder,
            baseUrl = "http://mock-api.com",
            retry = mockk(relaxed = true)
        )
    }

    @Test
    fun `fetchFromApi returns parsed season details`() = runTest {
        val season = "2020"
        val json = """{ "mock": "response" }"""

        every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(json)

        mockkObject(SeasonDetailsParser)
        every { SeasonDetailsParser.parseSeasonDetails(season, json) } returns listOf(
            SeasonDetail(
                season = season,
                round = "1",
                raceName = "Australian Grand Prix",
                date = "2020-03-15",
                winnerId = "hamilton",
                winnerName = "Lewis Hamilton",
                constructor = "Mercedes"
            )
        )

        val result = client.fetchFromApi(season)

        assertEquals(1, result.size)
        assertEquals("Australian Grand Prix", result[0].raceName)
    }
}
