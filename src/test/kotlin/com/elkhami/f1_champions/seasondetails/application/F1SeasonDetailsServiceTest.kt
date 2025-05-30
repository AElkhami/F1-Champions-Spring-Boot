package com.elkhami.f1_champions.seasondetails.application

import com.elkhami.f1_champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1_champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class F1SeasonDetailsServiceTest {

    private val seasonDetailsRepository = mockk<SeasonDetailsRepository>()
    private lateinit var service: F1SeasonDetailsService

    @BeforeTest
    fun setup() {
        service = F1SeasonDetailsService(seasonDetailsRepository)
    }

    @Test
    fun `getSeasonDetails returns mapped domain list`() {
        val season = "2020"
        val entity = SeasonDetailsEntity(
            season = "2020",
            round = "1",
            raceName = "Australian GP",
            date = "2020-03-15",
            winnerId = "hamilton",
            winnerName = "Lewis Hamilton",
            constructor = "Mercedes"
        )

        every { seasonDetailsRepository.findBySeason(season) } returns listOf(entity)

        val service = F1SeasonDetailsService(seasonDetailsRepository)
        val result = service.getSeasonDetails(season)

        assertEquals(1, result.size)
        assertEquals("Australian GP", result.first().raceName)
    }
}
