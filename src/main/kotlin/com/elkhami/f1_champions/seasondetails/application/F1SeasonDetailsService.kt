package com.elkhami.f1_champions.seasondetails.application

import com.elkhami.f1_champions.seasondetails.domain.SeasonDetailsRepository
import com.elkhami.f1_champions.seasondetails.domain.model.SeasonDetail
import com.elkhami.f1_champions.seasondetails.domain.service.SeasonDetailsService
import com.elkhami.f1_champions.seasondetails.intrastructure.mapper.toDomain
import org.springframework.stereotype.Service

@Service
class F1SeasonDetailsService(
    private val seasonDetailsRepository: SeasonDetailsRepository
) : SeasonDetailsService {

    override fun getSeasonDetails(season: String): List<SeasonDetail> =
        seasonDetailsRepository.findBySeason(season).map { it.toDomain() }

}
