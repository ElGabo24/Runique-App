package com.gapps.wear.app.presentation

import android.app.Application
import com.gapps.core.connectivity.data.di.coreConnectivityDataModule
import com.gapps.wear.app.presentation.di.appModule
import com.gapps.wear.run.data.di.wearRunDataModule
import com.gapps.wear.run.presentation.di.wearRunPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                appModule,
                wearRunPresentationModule,
                wearRunDataModule,
                coreConnectivityDataModule
            )
        }
    }
}