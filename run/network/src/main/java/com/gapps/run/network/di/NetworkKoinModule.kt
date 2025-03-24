package com.gapps.run.network.di

import com.gapps.core.domain.run.RemoteRunDataSource
import com.gapps.run.network.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()

}