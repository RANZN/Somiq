package com.ranjan.somiq.core.di

import org.koin.core.module.Module

actual class KotlinInitializer {
    actual fun init(additionalModules: List<Module>) {
        initKoin(
            additionalModules = additionalModules,
            appDeclaration = {
                // iOS doesn't need special initialization like Android
            }
        )
    }
}

