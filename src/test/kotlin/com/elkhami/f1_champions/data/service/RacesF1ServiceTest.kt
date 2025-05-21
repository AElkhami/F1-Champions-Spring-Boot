package com.elkhami.f1_champions.data.service

import com.elkhami.f1_champions.data.api.F1ApiClient
import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import com.elkhami.f1_champions.data.db.ChampionEntity
import com.elkhami.f1_champions.data.db.RaceResultEntity
import com.elkhami.f1_champions.data.repository.ChampionRepository
import com.elkhami.f1_champions.data.repository.RaceResultRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RacesF1ServiceTest {

    private lateinit var service: RacesF1Service

    private val api = mockk<F1ApiClient>()
    private val championRepo = mockk<ChampionRepository>()
    private val raceRepo = mockk<RaceResultRepository>()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = RacesF1Service(api, championRepo, raceRepo)
    }

    @Test
    fun `syncData should fetch and save new champions and race results`() {
        val champion = Champion("2021", "max", "Max Verstappen", "Red Bull")
        val race = RaceResult("2021", "1", "Bahrain GP", "2021-03-28", "max", "Max Verstappen", "Red Bull")

        every { api.fetchChampions() } returns listOf(champion)
        every { championRepo.findBySeason("2021") } returns null
        every { championRepo.save(any()) } returns ChampionEntity(
            season = "2021",
            driverId = "max",
            driverName = "Max Verstappen",
            constructor = "Red Bull"
        )

        every { raceRepo.findBySeason("2021") } returns emptyList()
        every { api.fetchRaceWinners("2021") } returns listOf(race)
        every { raceRepo.save(any()) } returns RaceResultEntity(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        service.syncData()

        verify { championRepo.save(match { it.season == "2021" }) }
        verify { api.fetchRaceWinners("2021") }
        verify { raceRepo.save(match { it.season == "2021" && it.raceName == "Bahrain GP" }) }
    }

    @Test
    fun `syncData should skip already existing champions and races`() {
        val champion = Champion("2020", "ham", "Lewis Hamilton", "Mercedes")

        every { api.fetchChampions() } returns listOf(champion)

        every { championRepo.findBySeason("2020") } returns ChampionEntity(
            season = "2020",
            driverId = "ham",
            driverName = "Lewis Hamilton",
            constructor = "Mercedes"
        )

        every { raceRepo.findBySeason("2020") } returns listOf(
            RaceResultEntity(
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
        every { raceRepo.save(any()) } returns RaceResultEntity(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        service.syncData()

        verify(exactly = 0) { championRepo.save(any()) }
        verify(exactly = 0) { api.fetchRaceWinners(any()) }
        verify(exactly = 0) { raceRepo.save(any()) }
    }

    @Test
    fun `getChampions should return domain champions`() {
        val entity = ChampionEntity(season = "2021", driverId = "max", driverName = "Max Verstappen", constructor = "Red Bull")

        every { championRepo.findAll() } returns listOf(entity)

        val result = service.getChampions()

        assert(result.size == 1)
        assert(result[0].season == "2021")
        assert(result[0].driverId == "max")
    }

    @Test
    fun `getRaceWinners should return domain race results`() {
        val entity = RaceResultEntity(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        every { raceRepo.findBySeason("2021") } returns listOf(entity)

        val result = service.getRaceWinners("2021")

        assert(result.size == 1)
        assert(result[0].raceName == "Bahrain GP")
    }
}
