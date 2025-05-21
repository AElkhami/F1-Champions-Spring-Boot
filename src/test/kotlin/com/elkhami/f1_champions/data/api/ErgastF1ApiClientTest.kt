package com.elkhami.f1_champions.data.api

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.*
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

class ErgastF1ApiClientTest {

    private lateinit var client: ErgastF1ApiClient

    private val builder = mockk<Builder>()
    private val webClient = mockk<WebClient>()
    private val requestSpec = mockk<RequestHeadersUriSpec<*>>()
    private val responseSpec = mockk<ResponseSpec>()

    @BeforeEach
    fun setUp() {
        every { builder.build() } returns webClient
        client = ErgastF1ApiClient(builder)
    }

    @Test
    fun `fetchChampions should call API and parse response`() {
        val rawJson = "fake-json"
        val expected = listOf(Champion("2021", "max", "Max Verstappen", "Red Bull"))

        mockkObject(ErgastParser)

        every { webClient.get() } returns requestSpec
        every { requestSpec.uri("https://ergast.com/api/f1/driverStandings/1.json?limit=1000") } returns requestSpec
        every { requestSpec.retrieve() } returns responseSpec
        every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(rawJson)
        every { ErgastParser.parseChampions(rawJson) } returns expected

        val result = client.fetchChampions()

        assertEquals(expected, result)

        unmockkObject(ErgastParser)
    }

    @Test
    fun `fetchRaceWinners should call API and parse response`() {
        val rawJson = "fake-json"
        val expected = listOf(RaceResult("2021", "1", "Bahrain GP", "2021-03-28", "max", "Max Verstappen", "Red Bull"))

        mockkObject(ErgastParser)

        every { webClient.get() } returns requestSpec
        every { requestSpec.uri("https://ergast.com/api/f1/2021/results/1.json") } returns requestSpec
        every { requestSpec.retrieve() } returns responseSpec
        every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(rawJson)
        every { ErgastParser.parseRaceWinners("2021", rawJson) } returns expected

        val result = client.fetchRaceWinners("2021")

        assertEquals(expected, result)

        unmockkObject(ErgastParser)
    }
}