package com.elkhami.f1champions

import com.elkhami.f1champions.sync.application.DataSeeder
import com.elkhami.f1champions.utils.loggerWithPrefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val dataSeeder: DataSeeder,
    private val scope: CoroutineScope,
) {
    private val logger = loggerWithPrefix()

    @EventListener(ApplicationReadyEvent::class)
    fun onAppReady() {
        logger.info("🚀 App started. Beginning data seeding...")
        scope.launch {
            try {
                dataSeeder.seed()
            } catch (e: Exception) {
                logger.info("❌ Failed during seeding: ${e.message}")
            }
        }
    }
}
