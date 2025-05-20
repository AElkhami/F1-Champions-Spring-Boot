package com.elkhami.f1_champions

import com.elkhami.f1_champions.data.service.RacesF1Service
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val racesF1Service: RacesF1Service
) {
    @EventListener(ApplicationReadyEvent::class)
    fun initOnStartup() {
        println("App started. Syncing F1 data from Ergast API...")
        racesF1Service.syncData()
    }
}