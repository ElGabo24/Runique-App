package com.gapps.core.test

import com.gapps.core.connectivity.domain.DeviceNode
import com.gapps.core.connectivity.domain.mesagging.MessagingAction
import com.gapps.core.connectivity.domain.mesagging.MessagingError
import com.gapps.core.domain.util.EmptyResult
import com.gapps.core.domain.util.Result
import com.gapps.run.domain.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PhoneToWatchConnectorFake : WatchConnector {

    var sendError: MessagingError? = null

    private val _isTrackable = MutableStateFlow(true)

    private val _connectedDevices = MutableStateFlow<DeviceNode?>(null)

    override val connectedDevices: StateFlow<DeviceNode?>
        get() = _connectedDevices.asStateFlow()

    private val _messagingActions = MutableSharedFlow<MessagingAction>()

    override val messagingActions: Flow<MessagingAction>
        get() = _messagingActions.asSharedFlow()

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return if (sendError == null) {
            Result.Success(Unit)
        }else {
            Result.Error(sendError!!)
        }
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        this._isTrackable.value = isTrackable
    }

    suspend fun sendFromWatchToPhone(action: MessagingAction){
        _messagingActions.emit(action)
    }
}