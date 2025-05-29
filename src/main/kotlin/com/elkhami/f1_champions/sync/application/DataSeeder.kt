package com.elkhami.f1_champions.sync.application

import com.elkhami.f1_champions.champions.domain.ChampionRepository
import com.elkhami.f1_champions.champions.infrastructure.api.ChampionsClient
import com.elkhami.f1_champions.champions.infrastructure.mapper.toEntity
import com.elkhami.f1_champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1_champions.seasondetails.intrastructure.api.SeasonDetailsClient
import com.elkhami.f1_champions.seasondetails.intrastructure.mapper.toEntity
import org.springframework.stereotype.Service

@Service
class DataSeeder(
    private val championsClient: ChampionsClient,
    private val seasonDetailsClient: SeasonDetailsClient,
    private val championRepository: ChampionRepository,
    private val seasonDetailsRepository: SeasonDetailsRepository
) {
    suspend fun seed(fromYear: Int = 2005, toYear: Int = 2025) {
        println("🚀 Seeding champions and season details ($fromYear–$toYear)")

        for (year in fromYear..toYear) {
            runCatching {
                val existingChampion = championRepository.findBySeason(year.toString())
                if (existingChampion == null) {
                    val champ = championsClient.fetchChampion(year)
                    if (champ != null) {
                        championRepository.save(champ.toEntity())
                        println("✅ Saved champion for $year")
                    }
                } else {
                    println("⏭️ Champion for $year already exists.")
                }

                val exists = seasonDetailsRepository.findBySeason(year.toString()).isNotEmpty()
                if (!exists) {
                    val winners = seasonDetailsClient.fetchSeasonDetails(year.toString())
                    winners.forEach { seasonDetailsRepository.save(it.toEntity()) }
                    println("✅ Saved ${winners.size} races for $year")
                } else {
                    println("⏭️ Season details for $year already exist.")
                }
            }.onFailure {
                println("❌ Error seeding season $year: ${it.message}")
            }
        }

        println("✅ Seeding complete.")
    }

}