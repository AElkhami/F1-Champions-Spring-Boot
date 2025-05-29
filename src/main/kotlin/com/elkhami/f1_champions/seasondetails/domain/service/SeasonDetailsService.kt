package com.elkhami.f1_champions.seasondetails.domain.service

import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail

interface SeasonDetailsService {
    fun getSeasonDetails(season: String): List<SeasonDetail>
}