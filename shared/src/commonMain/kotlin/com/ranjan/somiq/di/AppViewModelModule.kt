package com.ranjan.somiq.di

import com.ranjan.somiq.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
    viewModelOf(::SplashViewModel)
}