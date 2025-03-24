package com.gapps.analytics.data.di

import com.gapps.analytics.data.RoomAnalyticsRepository
import com.gapps.analytics.domain.AnalyticsRepository
import com.gapps.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single {
        get<RunDatabase>().analyticsDao
    }
}