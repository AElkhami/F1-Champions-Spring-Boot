package com.elkhami.f1champions.champions.application

import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.champions.domain.model.Champion
import com.elkhami.f1champions.champions.domain.service.ChampionsService
import com.elkhami.f1champions.champions.infrastructure.mapper.toDomain
import org.springframework.stereotype.Service

@Service
class F1ChampionsService(
    private val championRepo: ChampionRepository,
) : ChampionsService {
    override fun getChampions(): List<Champion> = championRepo.findAll().map { it.toDomain() }
}
