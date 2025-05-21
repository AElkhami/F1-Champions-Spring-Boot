package com.elkhami.f1_champions

import com.elkhami.f1_champions.data.service.RacesF1Service
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class DataInitializerTest {

    @Test
    fun `should call syncData on ApplicationReadyEvent`() {
        val mockService = mockk<RacesF1Service>(relaxed = true)
        val initializer = DataInitializer(mockService)

        initializer.initOnStartup()

        verify(exactly = 1) { mockService.syncData() }
    }
}
