package com.elkhami.f1_champions.seasondetails.infrastructure.api

import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1_champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1_champions.seasondetails.intrastructure.api.SeasonDetailsParser
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.*
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

class SeasonDetailsClientTest {

    private lateinit var client: SeasonDetailsClient

    private val builder = mockk<Builder>()
    private val webClient = mockk<WebClient>()
    private val requestSpec = mockk<RequestHeadersUriSpec<*>>()
    private val responseSpec = mockk<ResponseSpec>()

    @BeforeEach
    fun setUp() {
        every { builder.build() } returns webClient
        client = SeasonDetailsClient(builder)
    }

    @Test
    fun `fetchRaceWinners should call API and parse response`() {
        val rawJson = "fake-json"
        val expected = listOf(SeasonDetail("2021", "1", "Bahrain GP", "2021-03-28", "max", "Max Verstappen", "Red Bull"))

        mockkObject(SeasonDetailsParser)

        every { webClient.get() } returns requestSpec
        every { requestSpec.uri("https://ergast.com/api/f1/2021/results/1.json") } returns requestSpec
        every { requestSpec.retrieve() } returns responseSpec
        every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(rawJson)
        every { SeasonDetailsParser.parseSeasonDetails("2021", rawJson) } returns expected

        val result = client.fetchSeasonDetails("2021")

        assertEquals(expected, result)

        unmockkObject(SeasonDetailsParser)
    }
}