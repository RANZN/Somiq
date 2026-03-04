package com.ranjan.somiq.createpost.di

import com.ranjan.somiq.createpost.CreatePostViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createPostViewModelModule = module {
    viewModelOf(::CreatePostViewModel)
}
