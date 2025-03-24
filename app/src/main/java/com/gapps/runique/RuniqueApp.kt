package com.gapps.runique

import android.app.Application
import android.content.Context
import com.gapps.auth.data.di.authDataModule
import com.gapps.auth.presentation.di.authViewModelModule
import com.gapps.core.connectivity.data.di.coreConnectivityDataModule
import com.gapps.core.data.di.coreDataModule
import com.gapps.core.database.di.databaseModule
import com.gapps.run.data.di.runDataModule
import com.gapps.run.location.di.locationModule
import com.gapps.run.network.di.networkModule
import com.gapps.run.presentation.di.runPresentationModule
import com.gapps.runique.di.appModule
import com.google.android.play.core.splitcompat.SplitCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application()  {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                runDataModule,
                coreConnectivityDataModule,
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.installActivity(this)
    }

}