package com.gapps.analytics.data

import com.gapps.analytics.domain.AnalyticsRepository
import com.gapps.analytics.domain.AnalyticsValues
import com.gapps.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsRepository: AnalyticsDao
): AnalyticsRepository {

    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO){
            val totalDistance = async { analyticsRepository.getTotalDistance() }
            val totalTimeMillis = async { analyticsRepository.getTotalTimaRun() }
            val maxRunSpeed = async { analyticsRepository.getMaxRunSpeed() }
            val avgDistancePerRun = async { analyticsRepository.getAvgDistancePerRun() }
            val avgPacePerRun = async { analyticsRepository.getAvgPacePerRun() }

            AnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeMillis.await().milliseconds,
                fastestEverRun = maxRunSpeed.await(),
                avgDistancePerRun = avgDistancePerRun.await(),
                avgPacePerRun = avgPacePerRun.await()
            )
        }
    }

}