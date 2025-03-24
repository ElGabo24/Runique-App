package com.gapps.core.connectivity.data.mesaaging

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface MessagingActionDto {
    @Serializable
    data object StartorResume: MessagingActionDto
    @Serializable
    data object Pause: MessagingActionDto
    @Serializable
    data object Finish: MessagingActionDto
    @Serializable
    data object Trackable: MessagingActionDto
    @Serializable
    data object UnTrackable: MessagingActionDto
    @Serializable
    data object ConnectionRequest: MessagingActionDto
    @Serializable
    data class HeartRateUpdate(val heartRate: Int): MessagingActionDto
    @Serializable
    data class DistanceUpdate(val distanceMeters: Int): MessagingActionDto
    @Serializable
    data class TimeUpdate(val elapsedDuration: Duration): MessagingActionDto
}