package com.ranjan.somiq.feed.di

import com.ranjan.somiq.feed.ui.FeedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val feedViewModelModule = module {
    viewModelOf(::FeedViewModel)
}
