package com.elkhami.f1_champions.data.db

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class RaceResultEntity(
    @Id @GeneratedValue val id: Long = 0,
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val winnerId: String,
    val winnerName: String,
    val constructor: String
)

