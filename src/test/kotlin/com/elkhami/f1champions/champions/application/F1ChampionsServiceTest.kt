package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.champions.infrastructure.db.entity.ChampionEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class F1ChampionsServiceTest {
    private lateinit var service: ChampionsService

    private val championRepo = mockk<ChampionRepository>()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = F1ChampionsService(championRepo)
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
}
