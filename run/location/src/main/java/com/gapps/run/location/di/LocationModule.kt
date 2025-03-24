package com.gapps.run.location.di

import com.gapps.run.domain.LocationObsever
import com.gapps.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObsever>()
}