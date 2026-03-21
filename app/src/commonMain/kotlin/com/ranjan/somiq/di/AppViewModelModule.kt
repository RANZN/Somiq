package com.ranjan.somiq.di

import com.ranjan.somiq.collections.CollectionsViewModel
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.notifications.NotificationsViewModel
import com.ranjan.somiq.splash.SplashViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::CollectionsViewModel)
    
    singleOf(::GlobalEffectDispatcher)
}