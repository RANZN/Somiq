package com.ranjan.somiq.home.di

import org.koin.dsl.module

// Home module no longer has ViewModels - they are in feed, reels, profile, search modules
val homeViewModelModule = module {
    // Empty - home module is now just for navigation orchestration
}
