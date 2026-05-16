package com.ranjan.somiq.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.dsl.module

val androidModules = module {
    single<ChuckerInterceptor> {
        ChuckerInterceptor.Builder(get<Context>()).build()
    }
}