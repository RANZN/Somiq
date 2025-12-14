package com.ranjan.somiq.profile.di

import com.ranjan.somiq.profile.ui.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileViewModelModule = module {
    viewModelOf(::ProfileViewModel)
}
