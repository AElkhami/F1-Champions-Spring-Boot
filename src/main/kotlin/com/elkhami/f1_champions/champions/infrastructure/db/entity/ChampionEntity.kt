package com.elkhami.f1_champions.champions.infrastructure.db.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class ChampionEntity(
    @Id @GeneratedValue val id: Long = 0,
    val season: String,
    val driverId: String,
    val driverName: String,
    val constructor: String
)
