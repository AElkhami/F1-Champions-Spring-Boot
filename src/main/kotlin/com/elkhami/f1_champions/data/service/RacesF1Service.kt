package com.elkhami.f1_champions.data.service

import com.elkhami.f1_champions.data.api.F1ApiClient
import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import com.elkhami.f1_champions.data.mapper.toDomain
import com.elkhami.f1_champions.data.mapper.toEntity
import com.elkhami.f1_champions.data.repository.ChampionRepository
import com.elkhami.f1_champions.data.repository.RaceResultRepository
import com.elkhami.f1_champions.domain.service.RacesService
import org.springframework.stereotype.Service

@Service
class RacesF1Service(
    private val api: F1ApiClient,
    private val championRepo: ChampionRepository,
    private val raceRepo: RaceResultRepository
) : RacesService {

    override fun syncData() {
        val champions = api.fetchChampions().filter { it.season.toInt() in 2005..2025 }
        println("Fetched ${champions.size} champions")

        champions.forEach { champ ->
            if (championRepo.findBySeason(champ.season) == null) {
                println("Saving champion for season ${champ.season}")
                championRepo.save(champ.toEntity())
            }

            val existingRaces = raceRepo.findBySeason(champ.season)
            if (existingRaces.isEmpty()) {
                val winners = api.fetchRaceWinners(champ.season)
                println("Fetched ${winners.size} races for ${champ.season}")
                winners.forEach { winner ->
                    raceRepo.save(winner.toEntity())
                }
            } else {
                println("Races for season ${champ.season} already exist. Skipping...")
            }
        }

        println("Data sync complete.")
    }

    override fun getChampions(): List<Champion> =
        championRepo.findAll().map { it.toDomain() }

    override fun getRaceWinners(season: String): List<RaceResult> =
        raceRepo.findBySeason(season).map { it.toDomain() }
}
