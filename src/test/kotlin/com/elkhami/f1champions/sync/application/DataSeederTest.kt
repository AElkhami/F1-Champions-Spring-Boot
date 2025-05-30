package com.elkhami.f1champions.sync.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.infrastructure.api.ChampionsClient
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1champions.seasondetails.intrastructure.api.SeasonDetailsClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DataSeederTest {

    private val championsClient = mockk<ChampionsClient>()
    private val seasonDetailsClient = mockk<SeasonDetailsClient>()
    private val championRepository = mockk<ChampionRepository>()
    private val seasonDetailsRepository = mockk<SeasonDetailsRepository>()

    private lateinit var seeder: DataSeeder

    @BeforeTest
    fun setup() {
        seeder = DataSeeder(
            championsClient,
            seasonDetailsClient,
            championRepository,
            seasonDetailsRepository
        )
    }

    @Test
    fun `seed should save data when not found in DB`() = runTest {
        val year = 2020

        val champion = Champion(
            season = year.toString(),
            driverId = "hamilton",
            driverName = "Lewis Hamilton",
            constructor = "Mercedes"
        )

        val seasonDetail = SeasonDetail(
            season = year.toString(),
            round = "1",
            raceName = "Australian GP",
            date = "2020-03-15",
            winnerId = "hamilton",
            winnerName = "Lewis Hamilton",
            constructor = "Mercedes"
        )

        coEvery { championRepository.findBySeason(year.toString()) } returns null
        coEvery { championsClient.fetchChampion(year) } returns champion
        coEvery { championRepository.save(any()) } returns mockk()

        coEvery { seasonDetailsRepository.findBySeason(year.toString()) } returns emptyList()
        coEvery { seasonDetailsClient.fetchSeasonDetails(year.toString()) } returns listOf(seasonDetail)
        coEvery { seasonDetailsRepository.save(any()) } returns mockk()

        seeder.seed(fromYear = year, toYear = year)

        coVerify {
            championRepository.save(
                match {
                    it.season == "2020" &&
                            it.driverId == "hamilton" &&
                            it.driverName == "Lewis Hamilton" &&
                            it.constructor == "Mercedes"
                }
            )
        }

        coVerify {
            seasonDetailsRepository.save(
                match {
                    it.season == "2020" &&
                            it.winnerId == "hamilton" &&
                            it.winnerName == "Lewis Hamilton" &&
                            it.constructor == "Mercedes"
                }
            )
        }
    }


    @Test
    fun `seed should skip saving if data exists in DB`() = runTest {
        val year = 2021

        coEvery { championRepository.findBySeason(year.toString()) } returns mockk()
        coEvery { seasonDetailsRepository.findBySeason(year.toString()) } returns listOf(mockk())

        seeder.seed(fromYear = year, toYear = year)

        coVerify(exactly = 0) { championsClient.fetchChampion(any()) }
        coVerify(exactly = 0) { seasonDetailsClient.fetchSeasonDetails(any()) }
        coVerify(exactly = 0) { championRepository.save(any()) }
        coVerify(exactly = 0) { seasonDetailsRepository.save(any()) }
    }

    @Test
    fun `seed should handle and log errors`() = runTest {
        val year = 2022

        coEvery { championRepository.findBySeason(year.toString()) } throws RuntimeException("DB error")

        seeder.seed(fromYear = year, toYear = year)

        coVerify(exactly = 0) { championsClient.fetchChampion(any()) }
        coVerify(exactly = 0) { championRepository.save(any()) }
    }
}
