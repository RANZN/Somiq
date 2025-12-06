package com.ranjan.somiq.home.di

import com.ranjan.somiq.home.ui.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModelOf(::HomeViewModel)
}
