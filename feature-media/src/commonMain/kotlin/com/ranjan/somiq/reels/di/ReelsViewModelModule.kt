package com.ranjan.somiq.reels.di

import com.ranjan.somiq.reels.ui.ReelsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val reelsViewModelModule = module {
    viewModelOf(::ReelsViewModel)
}
