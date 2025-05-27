package com.elkhami.f1_champions.champions.domain.service

import com.elkhami.f1_champions.champions.domain.model.Champion

interface ChampionsService {
    fun getChampions(): List<Champion>
}