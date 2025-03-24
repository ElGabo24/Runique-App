package com.gapps.core.connectivity.data.mesaaging

import com.gapps.core.connectivity.domain.mesagging.MessagingAction

fun MessagingAction.toMessagingActionDto(): MessagingActionDto {
    return when(this){
        MessagingAction.ConnectionRequest -> MessagingActionDto.ConnectionRequest
        is MessagingAction.DistanceUpdate -> MessagingActionDto.DistanceUpdate(distanceMeters)
        MessagingAction.Finish -> MessagingActionDto.Finish
        is MessagingAction.HeartRateUpdate -> MessagingActionDto.HeartRateUpdate(heartRate)
        MessagingAction.Pause -> MessagingActionDto.Pause
        MessagingAction.StartorResume -> MessagingActionDto.StartorResume
        is MessagingAction.TimeUpdate -> MessagingActionDto.TimeUpdate(elapsedDuration)
        MessagingAction.Trackable -> MessagingActionDto.Trackable
        MessagingAction.UnTrackable -> MessagingActionDto.UnTrackable
    }
}
fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when(this){
        MessagingActionDto.ConnectionRequest -> MessagingAction.ConnectionRequest
        is MessagingActionDto.DistanceUpdate -> MessagingAction.DistanceUpdate(distanceMeters)
        MessagingActionDto.Finish -> MessagingAction.Finish
        is MessagingActionDto.HeartRateUpdate -> MessagingAction.HeartRateUpdate(heartRate)
        MessagingActionDto.Pause -> MessagingAction.Pause
        MessagingActionDto.StartorResume -> MessagingAction.StartorResume
        is MessagingActionDto.TimeUpdate -> MessagingAction.TimeUpdate(elapsedDuration)
        MessagingActionDto.Trackable -> MessagingAction.Trackable
        MessagingActionDto.UnTrackable -> MessagingAction.UnTrackable
    }
}