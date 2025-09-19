package com.ranjan.smartcents.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

expect class KotlinInitializer {
    fun init()
}

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(appModules, viewmodelModules, sharedModules)
}