package com.gapps.run.domain

import com.gapps.core.connectivity.domain.DeviceNode
import com.gapps.core.connectivity.domain.mesagging.MessagingAction
import com.gapps.core.connectivity.domain.mesagging.MessagingError
import com.gapps.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevices: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError>
    fun setIsTrackable(isTrackable: Boolean)
}