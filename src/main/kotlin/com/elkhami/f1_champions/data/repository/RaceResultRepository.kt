package com.elkhami.f1_champions.data.repository

import com.elkhami.f1_champions.data.db.RaceResultEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RaceResultRepository: JpaRepository<RaceResultEntity, Long> {
    fun findBySeason(season: String): List<RaceResultEntity>
}