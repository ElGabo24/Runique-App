package com.gapps.run.data.di

import com.gapps.core.domain.run.SyncRunScheduler
import com.gapps.run.data.CreateRunWorker
import com.gapps.run.data.DeleteRunWorker
import com.gapps.run.data.FetchRunsWorker
import com.gapps.run.data.SyncRunWorkerScheduler
import com.gapps.run.data.connectivity.PhoneToWatchConnector
import com.gapps.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}