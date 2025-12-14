package com.ranjan.somiq.search.di

import com.ranjan.somiq.search.ui.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModelOf(::SearchViewModel)
}
