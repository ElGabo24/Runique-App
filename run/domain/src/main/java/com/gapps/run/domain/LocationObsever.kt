package com.gapps.run.domain

import com.gapps.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObsever {
    fun observeLocation(interval: Long): Flow<LocationWithAltitude>
}