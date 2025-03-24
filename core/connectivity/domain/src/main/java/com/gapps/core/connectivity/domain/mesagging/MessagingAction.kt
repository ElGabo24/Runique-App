package com.gapps.core.connectivity.domain.mesagging

import kotlin.time.Duration

sealed interface MessagingAction {
    data object StartorResume: MessagingAction
    data object Pause: MessagingAction
    data object Finish: MessagingAction
    data object Trackable: MessagingAction
    data object UnTrackable: MessagingAction
    data object ConnectionRequest: MessagingAction
    data class HeartRateUpdate(val heartRate: Int): MessagingAction
    data class DistanceUpdate(val distanceMeters: Int): MessagingAction
    data class TimeUpdate(val elapsedDuration: Duration): MessagingAction
}