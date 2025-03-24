package com.gapps.core.connectivity.domain.mesagging

import com.gapps.core.domain.util.Error

enum class MessagingError: Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}
