package com.ranjan.somiq.createpost.di

import com.ranjan.somiq.createpost.CreatePostViewModel
import com.ranjan.somiq.createpost.PostUploadManager
import com.ranjan.somiq.core.domain.PostUploadService
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createPostViewModelModule = module {
    single<PostUploadService> { PostUploadManager(get()) }
    viewModelOf(::CreatePostViewModel)
}
