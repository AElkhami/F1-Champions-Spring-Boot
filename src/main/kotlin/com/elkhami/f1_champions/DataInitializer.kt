package com.elkhami.f1_champions

import com.elkhami.f1_champions.sync.application.DataSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val dataSeeder: DataSeeder,
    private val scope: CoroutineScope
) {
    @EventListener(ApplicationReadyEvent::class)
    fun onAppReady() {
        println("🚀 App started. Beginning data seeding...")
        scope.launch {
            try {
                dataSeeder.seed()
            } catch (e: Exception) {
                println("❌ Failed during seeding: ${e.message}")
            }
        }
    }
}
