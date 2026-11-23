package com.ranjan.somiq.core.di

import com.ranjan.somiq.core.data.PostUploadServiceImpl
import com.ranjan.somiq.core.domain.PostUploadService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::PostUploadServiceImpl) bind PostUploadService::class
}