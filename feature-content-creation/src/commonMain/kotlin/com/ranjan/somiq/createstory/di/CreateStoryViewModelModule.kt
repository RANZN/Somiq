package com.ranjan.somiq.createstory.di

import com.ranjan.somiq.createstory.CreateStoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createStoryViewModelModule = module {
    viewModelOf(::CreateStoryViewModel)
}
