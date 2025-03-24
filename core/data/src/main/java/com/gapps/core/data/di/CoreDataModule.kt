package com.gapps.core.data.di

import com.gapps.core.data.auth.EncryptedSessionStorage
import com.gapps.core.data.networking.HttpClientFactory
import com.gapps.core.data.run.OfflineFirstRunRepository
import com.gapps.core.domain.SessionStorage
import com.gapps.core.domain.run.RunRepository
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single{
        HttpClientFactory(get()).build(CIO.create())
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}