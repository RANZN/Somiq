package com.ranjan.somiq.di

actual class KotlinInitializer {
    actual fun init() {
        initKoin {
            modules(appModules)
        }
    }
}