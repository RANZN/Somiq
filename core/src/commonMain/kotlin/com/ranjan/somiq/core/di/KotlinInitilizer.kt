package com.ranjan.somiq.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

expect class KotlinInitializer {
    fun init(additionalModules: List<Module>)
}

fun initKoin(appDeclaration: KoinAppDeclaration, additionalModules: List<Module> = emptyList()) = startKoin {
    appDeclaration()
    modules(
        coreModule,
        coreViewModelModule,
        networkModule,
        *additionalModules.toTypedArray()
    )
}