package com.ranjan.somiq.di

import com.ranjan.somiq.collections.CollectionsViewModel
import com.ranjan.somiq.notifications.NotificationsViewModel
import com.ranjan.somiq.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::CollectionsViewModel)
}