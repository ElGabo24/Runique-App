@file:OptIn(ExperimentalCoroutinesApi::class)

package com.gapps.wear.run.data

import com.gapps.core.connectivity.domain.DeviceNode
import com.gapps.core.connectivity.domain.DeviceType
import com.gapps.core.connectivity.domain.NodeDiscovery
import com.gapps.core.connectivity.domain.mesagging.MessagingAction
import com.gapps.core.connectivity.domain.mesagging.MessagingClient
import com.gapps.core.connectivity.domain.mesagging.MessagingError
import com.gapps.core.domain.util.EmptyResult
import com.gapps.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

class WatchToPhoneConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
) : PhoneConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)

    override val connectedNode = _connectedNode.asStateFlow()

    override val messagingActions: Flow<MessagingAction> = nodeDiscovery
        .observeConnectedDevices(DeviceType.WATCH)
        .flatMapLatest { connectedNodes ->
            val node = connectedNodes.firstOrNull()
            if (node != null && node.isNearby) {
                _connectedNode.value = node
                messagingClient.connectToNode(node.id)
            } else flowOf()
        }
        .shareIn(
            applicationScope,
            SharingStarted.Eagerly
        )

    override suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }
}