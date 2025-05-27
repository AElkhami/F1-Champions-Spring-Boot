package com.elkhami.f1_champions.seasondetails.application

import com.elkhami.f1_champions.champions.domain.ChampionRepository
import com.elkhami.f1_champions.champions.domain.model.Champion
import com.elkhami.f1_champions.champions.infrastructure.api.ChampionsClient
import com.elkhami.f1_champions.champions.infrastructure.db.entity.ChampionEntity
import com.elkhami.f1_champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1_champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1_champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1_champions.seasondetails.intrastructure.db.entity.SeasonDetailsEntity
import com.elkhami.f1_champions.seasondetails.intrastructure.mapper.toEntity
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class F1SeasonDetailsServiceTest {

    private lateinit var seasonDetailsService: SeasonDetailsService

    private val seasonDetailsClient = mockk<SeasonDetailsClient>()
    private val championsClient = mockk<ChampionsClient>()

    private val seasonDetailsRepo = mockk<SeasonDetailsRepository>()
    private val championRepo = mockk<ChampionRepository>()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        seasonDetailsService =
            F1SeasonDetailsService(championsClient, seasonDetailsClient, championRepo, seasonDetailsRepo)
    }

    @Test
    fun `syncData should fetch and save new champions and race results`() {
        val champion = Champion("2021", "max", "Max Verstappen", "Red Bull")
        val seasonDetail = SeasonDetail("2021", "1", "Bahrain GP", "2021-03-28", "max", "Max Verstappen", "Red Bull")

        every { championsClient.fetchChampions() } returns listOf(champion)
        every { championRepo.findBySeason("2021") } returns null
        every { championRepo.save(any()) } returns ChampionEntity(
            season = "2021",
            driverId = "max",
            driverName = "Max Verstappen",
            constructor = "Red Bull"
        )

        every { seasonDetailsRepo.findBySeason("2021") } returns emptyList()
        every { seasonDetailsClient.fetchSeasonDetails("2021") } returns listOf(seasonDetail)
        every { seasonDetailsRepo.save(any()) } returns SeasonDetailsEntity(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        seasonDetailsService.syncData()

        verify { championRepo.save(match { it.season == "2021" }) }
        verify { seasonDetailsClient.fetchSeasonDetails("2021") }
        verify { seasonDetailsRepo.save(match { it.season == "2021" && it.raceName == "Bahrain GP" }) }
    }

    @Test
    fun `syncData should skip already existing champions and races`() {
        val champion = Champion("2020", "ham", "Lewis Hamilton", "Mercedes")

        every { championsClient.fetchChampions() } returns listOf(champion)

        every { championRepo.findBySeason("2020") } returns ChampionEntity(
            season = "2020",
            driverId = "ham",
            driverName = "Lewis Hamilton",
            constructor = "Mercedes"
        )

        every { seasonDetailsRepo.findBySeason("2020") } returns listOf(
            SeasonDetailsEntity(
                season = "2020",
                round = "1",
                raceName = "Austria GP",
                date = "2020-07-05",
                winnerId = "ham",
                winnerName = "Lewis Hamilton",
                constructor = "Mercedes"
            )
        )

        every { championRepo.save(any()) } returns ChampionEntity(
            season = "2021",
            driverId = "max",
            driverName = "Max Verstappen",
            constructor = "Red Bull"
        )
        every { seasonDetailsRepo.save(any()) } returns SeasonDetailsEntity(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        seasonDetailsService.syncData()

        verify(exactly = 0) { championRepo.save(any()) }
        verify(exactly = 0) { seasonDetailsClient.fetchSeasonDetails(any()) }
        verify(exactly = 0) { seasonDetailsRepo.save(any()) }
    }


    @Test
    fun `getRaceWinners should return domain race results`() {
        val entity = SeasonDetail(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        every { seasonDetailsRepo.findBySeason("2021") } returns listOf(entity.toEntity())

        val result = seasonDetailsService.getSeasonDetails("2021")

        assert(result.size == 1)
        assert(result[0].raceName == "Bahrain GP")
    }
}
