package com.elkhami.f1_champions.data.repository

import com.elkhami.f1_champions.data.db.ChampionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChampionRepository: JpaRepository<ChampionEntity, Long> {
    fun findBySeason(season: String): ChampionEntity?
}