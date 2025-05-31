package com.ranjan.smartcents.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

actual class KotlinInitializer(private val context: Context) {
    actual fun init() {
        startKoin {
            androidContext(context)
            androidLogger()
            modules(appModule, sharedModules)
        }
    }
}