package com.gapps.core.connectivity.data

import com.gapps.core.connectivity.domain.DeviceNode
import com.google.android.gms.wearable.Node

fun Node.toDeviceNode(): DeviceNode {
    return DeviceNode(
        id = id,
        displayName = displayName,
        isNearby = isNearby
    )
}