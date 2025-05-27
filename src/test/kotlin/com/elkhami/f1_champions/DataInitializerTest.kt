package com.elkhami.f1_champions

import com.elkhami.f1_champions.seasondetails.application.F1SeasonDetailsService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class DataInitializerTest {

    @Test
    fun `should call syncData on ApplicationReadyEvent`() {
        val mockService = mockk<F1SeasonDetailsService>(relaxed = true)
        val initializer = DataInitializer(mockService)

        initializer.initOnStartup()

        verify(exactly = 1) { mockService.syncData() }
    }
}
