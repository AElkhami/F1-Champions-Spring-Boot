package com.elkhami.f1_champions.champions.infrastructure.db.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "champion_entity")
data class ChampionEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val season: String,
    @Column(name = "driver_id")
    val driverId: String,
    @Column(name = "driver_name")
    val driverName: String,
    val constructor: String
)