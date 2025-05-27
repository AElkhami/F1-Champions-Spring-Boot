package com.elkhami.f1_champions

import com.elkhami.f1_champions.seasondetails.application.F1SeasonDetailsService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val seasonDetailsService: F1SeasonDetailsService
) {
    @EventListener(ApplicationReadyEvent::class)
    fun initOnStartup() {
        println("App started. Syncing F1 data from Ergast API...")
        seasonDetailsService.syncData()
    }
}