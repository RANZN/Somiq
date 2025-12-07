package com.ranjan.somiq.di

import com.ranjan.somiq.profile.ui.ProfileViewModel
import com.ranjan.somiq.reels.ui.ReelsViewModel
import com.ranjan.somiq.search.ui.SearchViewModel
import com.ranjan.somiq.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appViewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ReelsViewModel)
    viewModelOf(::SearchViewModel)
}