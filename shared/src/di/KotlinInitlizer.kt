package com.ranjan.somiq.di

import com.ranjan.somiq.core.di.coreModule
import com.ranjan.somiq.core.di.networkModule
import com.ranjan.somiq.core.di.platformModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    appDeclaration()
    modules(platformModules)
    modules(localDatabaseModule)
    modules(networkModule)
    modules(coreModule)
    modules(sharedModules)
}
