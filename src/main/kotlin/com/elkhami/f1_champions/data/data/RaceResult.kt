package com.elkhami.f1_champions.data.data

data class RaceResult(
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val winnerId: String,
    val winnerName: String,
    val constructor: String
)
