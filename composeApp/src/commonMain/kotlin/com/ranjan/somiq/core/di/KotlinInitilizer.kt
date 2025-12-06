package com.ranjan.somiq.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

expect class KotlinInitializer {
    fun init()
}

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(appModules, viewmodelModules, networkModule)
}