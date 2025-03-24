package com.gapps.run.presentation.runOverview.mapper

import com.gapps.core.domain.run.Run
import com.gapps.core.presentation.ui.formatted
import com.gapps.core.presentation.ui.toFormattedHeartRate
import com.gapps.core.presentation.ui.toFormattedKm
import com.gapps.core.presentation.ui.toFormattedKmh
import com.gapps.core.presentation.ui.toFormattedMeters
import com.gapps.core.presentation.ui.toFormattedPace
import com.gapps.run.presentation.runOverview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)
    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!! ,
        duration = duration.formatted(),
        datetime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate.toFormattedHeartRate(),
        maxHeartRate = maxHeartRate.toFormattedHeartRate()
    )

}