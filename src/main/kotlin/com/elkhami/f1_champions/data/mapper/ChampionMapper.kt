package com.elkhami.f1_champions.data.mapper

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import com.elkhami.f1_champions.data.db.ChampionEntity
import com.elkhami.f1_champions.data.db.RaceResultEntity

fun Champion.toEntity() = ChampionEntity(
    season = this.season,
    driverId = this.driverId,
    driverName = this.driverName,
    constructor = this.constructor
)

fun ChampionEntity.toDomain() = Champion(
    season = this.season,
    driverId = this.driverId,
    driverName = this.driverName,
    constructor = this.constructor
)

fun RaceResult.toEntity() = RaceResultEntity(
    season = this.season,
    round = this.round,
    raceName = this.raceName,
    date = this.date,
    winnerId = this.winnerId,
    winnerName = this.winnerName,
    constructor = this.constructor
)

fun RaceResultEntity.toDomain() = RaceResult(
    season = this.season,
    round = this.round,
    raceName = this.raceName,
    date = this.date,
    winnerId = this.winnerId,
    winnerName = this.winnerName,
    constructor = this.constructor
)
