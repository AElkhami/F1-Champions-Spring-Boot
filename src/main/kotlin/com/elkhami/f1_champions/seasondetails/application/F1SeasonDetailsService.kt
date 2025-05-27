package com.elkhami.f1_champions.seasondetails.application

import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1_champions.seasondetails.intrastructure.mapper.toDomain
import com.elkhami.f1_champions.seasondetails.intrastructure.mapper.toEntity
import com.elkhami.f1_champions.champions.domain.ChampionRepository
import com.elkhami.f1_champions.champions.infrastructure.api.ChampionsClient
import com.elkhami.f1_champions.champions.infrastructure.mapper.toEntity
import com.elkhami.f1_champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1_champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1_champions.seasondetails.intrastructure.api.SeasonDetailsClient
import org.springframework.stereotype.Service

@Service
class F1SeasonDetailsService(
    private val championsClient: ChampionsClient,
    private val seasonDetailsClient: SeasonDetailsClient,
    private val championRepo: ChampionRepository,
    private val seasonDetailsRepository: SeasonDetailsRepository
) : SeasonDetailsService {

    override fun getSeasonDetails(season: String): List<SeasonDetail> =
        seasonDetailsRepository.findBySeason(season).map { it.toDomain() }

    override fun syncData() {
        val champions = championsClient.fetchChampions().filter { it.season.toInt() in 2005..2025 }
        println("Fetched ${champions.size} champions")

        champions.forEach { champ ->
            if (championRepo.findBySeason(champ.season) == null) {
                println("Saving champion for season ${champ.season}")
                championRepo.save(champ.toEntity())
            }

            val existingSeasons = seasonDetailsRepository.findBySeason(champ.season)
            if (existingSeasons.isEmpty()) {
                val winners = seasonDetailsClient.fetchSeasonDetails(champ.season)
                println("Fetched ${winners.size} races for ${champ.season}")
                winners.forEach { winner ->
                    seasonDetailsRepository.save(winner.toEntity())
                }
            } else {
                println("Races for season ${champ.season} already exist. Skipping...")
            }
        }

        println("Data sync complete.")
    }
}
