package com.gapps.core.test

import com.gapps.core.domain.location.LocationWithAltitude
import com.gapps.run.domain.LocationObsever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LocationObserverFake: LocationObsever {

    private val _locations = MutableSharedFlow<LocationWithAltitude>()

    override fun observeLocation(interval: Long): Flow<LocationWithAltitude> {
        return _locations.asSharedFlow()
    }

    suspend fun trackLocation(location: LocationWithAltitude){
        _locations.emit(location)
    }
}