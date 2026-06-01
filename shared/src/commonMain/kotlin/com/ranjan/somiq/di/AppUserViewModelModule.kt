package com.ranjan.somiq.di

import com.ranjan.somiq.notifications.NotificationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appUserViewModelModule = module {
    viewModelOf(::NotificationsViewModel)
}
