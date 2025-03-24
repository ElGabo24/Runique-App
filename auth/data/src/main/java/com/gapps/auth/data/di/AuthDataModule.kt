package com.gapps.auth.data.di

import com.gapps.auth.data.AuthRepositoryImpl
import com.gapps.auth.data.EmailPatternValidator
import com.gapps.auth.domain.AuthRepository
import com.gapps.auth.domain.PatternValidator
import com.gapps.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator()
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}