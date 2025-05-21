package com.elkhami.f1_champions.data.mapper

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityMapperTest {

    @Test
    fun `Champion toEntity and back should preserve data`() {
        val domain = Champion(
            season = "2021",
            driverId = "max",
            driverName = "Max Verstappen",
            constructor = "Red Bull"
        )

        val entity = domain.toEntity()
        val result = entity.toDomain()

        assertEquals(domain, result)
    }

    @Test
    fun `RaceResult toEntity and back should preserve data`() {
        val domain = RaceResult(
            season = "2021",
            round = "1",
            raceName = "Bahrain GP",
            date = "2021-03-28",
            winnerId = "max",
            winnerName = "Max Verstappen",
            constructor = "Red Bull"
        )

        val entity = domain.toEntity()
        val result = entity.toDomain()

        assertEquals(domain, result)
    }
}
