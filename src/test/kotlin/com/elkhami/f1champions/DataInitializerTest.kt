package com.elkhami.f1champions

import com.elkhami.f1champions.sync.application.DataSeeder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class DataInitializerTest {
    private val dataSeeder = mockk<DataSeeder>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    private lateinit var dataInitializer: DataInitializer

    @BeforeTest
    fun setup() {
        dataInitializer = DataInitializer(dataSeeder, testScope)
    }

    @Test
    fun `onAppReady should call dataSeeder seed`() =
        runTest {
            dataInitializer.onAppReady()
            testDispatcher.scheduler.advanceUntilIdle()
            coVerify(exactly = 1) { dataSeeder.seed() }
        }

    @Test
    fun `onAppReady should handle exception from dataSeeder seed`() =
        runTest {
            coEvery { dataSeeder.seed() } throws RuntimeException("Test exception")
            dataInitializer.onAppReady()
            testDispatcher.scheduler.advanceUntilIdle()
            coVerify(exactly = 1) { dataSeeder.seed() }
        }
}
