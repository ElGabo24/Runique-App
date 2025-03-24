@file:OptIn(ExperimentalFoundationApi::class)

package com.gapps.auth.presentation.di

import androidx.compose.foundation.ExperimentalFoundationApi
import com.gapps.auth.presentation.login.LoginViewModel
import com.gapps.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}