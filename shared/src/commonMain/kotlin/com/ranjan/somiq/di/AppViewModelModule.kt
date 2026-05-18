package com.ranjan.somiq.di

import com.ranjan.somiq.app.AppViewModel
import com.ranjan.somiq.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::SplashViewModel)
}