package com.gapps.run.presentation.runOverview.model

data class RunUi(
    val id: String,
    val duration: String,
    val datetime: String,
    val distance: String,
    val avgSpeed: String,
    val maxSpeed: String,
    val pace: String,
    val totalElevation: String,
    val mapPictureUrl: String?,
    val avgHeartRate: String,
    val maxHeartRate: String
)
