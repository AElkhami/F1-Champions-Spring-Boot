package com.elkhami.f1_champions.champions.infrastructure.api

import com.elkhami.f1_champions.champions.domain.model.Champion
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.*
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

class ChampionsClientTest {

    private lateinit var client: ChampionsClient

    private val builder = mockk<Builder>()
    private val webClient = mockk<WebClient>()
    private val requestSpec = mockk<RequestHeadersUriSpec<*>>()
    private val responseSpec = mockk<ResponseSpec>()

    @BeforeEach
    fun setUp() {
        every { builder.build() } returns webClient
        client = ChampionsClient(builder)
    }

    @Test
    fun `fetchChampions should call API and parse response`() {
        val rawJson = "fake-json"
        val expected = listOf(Champion("2021", "max", "Max Verstappen", "Red Bull"))

        mockkObject(ChampionParser)

        every { webClient.get() } returns requestSpec
        every { requestSpec.uri("https://ergast.com/api/f1/driverStandings/1.json?limit=1000") } returns requestSpec
        every { requestSpec.retrieve() } returns responseSpec
        every { responseSpec.bodyToMono(String::class.java) } returns Mono.just(rawJson)
        every { ChampionParser.parseChampions(rawJson) } returns expected

        val result = client.fetchChampions()

        assertEquals(expected, result)

        unmockkObject(ChampionParser)
    }
}