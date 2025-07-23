package com.ranjan.smartcents.di

actual class KotlinInitializer {
    actual fun init() {
        initKoin {
            modules(appModule)
        }
    }
}