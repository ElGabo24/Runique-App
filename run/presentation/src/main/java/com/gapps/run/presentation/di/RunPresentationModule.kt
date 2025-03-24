package com.gapps.run.presentation.di

import com.gapps.run.domain.RunningTracker
import com.gapps.run.presentation.activeRun.ActiveRunViewModel
import com.gapps.run.presentation.runOverview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)
    single {
        get<RunningTracker>().elapsedTime
    }

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}