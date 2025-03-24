package com.gapps.core.connectivity.data.di

import com.gapps.core.connectivity.data.WearNodeDiscovery
import com.gapps.core.connectivity.data.mesaaging.WearMessagingClient
import com.gapps.core.connectivity.domain.NodeDiscovery
import com.gapps.core.connectivity.domain.mesagging.MessagingClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearMessagingClient).bind<MessagingClient>()
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
}