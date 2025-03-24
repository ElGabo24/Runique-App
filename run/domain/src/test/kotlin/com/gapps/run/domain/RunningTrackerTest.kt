@file:OptIn(ExperimentalCoroutinesApi::class)

package com.gapps.run.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import com.gapps.core.connectivity.domain.mesagging.MessagingAction
import com.gapps.core.domain.location.Location
import com.gapps.core.domain.location.LocationWithAltitude
import com.gapps.core.test.LocationObserverFake
import com.gapps.core.test.MainCoroutineExtension
import com.gapps.core.test.PhoneToWatchConnectorFake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.math.roundToInt

class RunningTrackerTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var runningTracker: RunningTracker
    private lateinit var locationOserverFake: LocationObserverFake
    private lateinit var phoneToWatchConnectorFake: PhoneToWatchConnectorFake

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: CoroutineScope

    @BeforeEach
    fun setUp() {
        locationOserverFake = LocationObserverFake()
        phoneToWatchConnectorFake = PhoneToWatchConnectorFake()

        testDispatcher = mainCoroutineExtension.testDispatcher
        testScope = CoroutineScope(testDispatcher)
        
        runningTracker = RunningTracker(
            locationObsever = locationOserverFake,
            applicationScope = testScope,
            watchConnector = phoneToWatchConnectorFake
        )
    }

    @Test
    fun testCombiningRunData() = runTest {
        runningTracker.runData.test {
            skipItems(1)

            runningTracker.startObserveringLocation()
            runningTracker.setIsTracking(true)

            val location1 = LocationWithAltitude(Location(1.0, 1.0), 1.0)
            locationOserverFake.trackLocation(location1)
            val emission1 = awaitItem()

            val location2 = LocationWithAltitude(Location(2.0, 2.0), 2.0)
            locationOserverFake.trackLocation(location2)
            val emission2 = awaitItem()

            phoneToWatchConnectorFake.sendFromWatchToPhone(MessagingAction.HeartRateUpdate(160))
            val emission3 = awaitItem()

            phoneToWatchConnectorFake.sendFromWatchToPhone(MessagingAction.HeartRateUpdate(170))
            val emission4 = awaitItem()

            testScope.cancel()

            assertThat(emission1.locations[0][0].location).isEqualTo(location1)
            assertThat(emission2.locations[0][1].location).isEqualTo(location2)
            assertThat(emission3.heartRates).isEqualTo(listOf(160))
            assertThat(emission4.heartRates).isEqualTo(listOf(160, 170))

            val expectedDistance = 156.9 * 1000L
            val tolerance = 0.03
            val lowerBound = (expectedDistance * (1 - tolerance)).roundToInt()
            val upperBound = (expectedDistance * (1 + tolerance)).roundToInt()
            assertThat(emission4.distanceMeters).isBetween(lowerBound, upperBound)

            assertThat(emission4.locations.first()).hasSize(2)
        }
    }
}