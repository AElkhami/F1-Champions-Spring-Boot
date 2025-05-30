package com.elkhami.f1champions.champions.domain.service

import com.elkhami.f1champions.champions.domain.model.Champion

interface ChampionsService {
    fun getChampions(): List<Champion>
}
