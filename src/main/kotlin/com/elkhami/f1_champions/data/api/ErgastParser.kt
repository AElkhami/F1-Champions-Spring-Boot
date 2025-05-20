package com.elkhami.f1_champions.data.api

import com.elkhami.f1_champions.data.data.Champion
import com.elkhami.f1_champions.data.data.RaceResult
import com.fasterxml.jackson.databind.ObjectMapper

object ErgastParser {
    fun parseChampions(json: String?): List<Champion> {
        val root = ObjectMapper().readTree(json)
        return root["MRData"]["StandingsTable"]["StandingsLists"].map {
            val driver = it["DriverStandings"][0]["Driver"]
            val constructor = it["DriverStandings"][0]["Constructors"][0]
            Champion(
                season = it["season"].asText(),
                driverId = driver["driverId"].asText(),
                driverName = "${driver["givenName"].asText()} ${driver["familyName"].asText()}",
                constructor = constructor["name"].asText()
            )
        }
    }

    fun parseRaceWinners(season: String, json: String?): List<RaceResult> {
        val root = ObjectMapper().readTree(json)
        return root["MRData"]["RaceTable"]["Races"].map {
            val winner = it["Results"][0]["Driver"]
            val constructor = it["Results"][0]["Constructor"]
            RaceResult(
                season = season,
                round = it["round"].asText(),
                raceName = it["raceName"].asText(),
                date = it["date"].asText(),
                winnerId = winner["driverId"].asText(),
                winnerName = "${winner["givenName"].asText()} ${winner["familyName"].asText()}",
                constructor = constructor["name"].asText()
            )
        }
    }
}
