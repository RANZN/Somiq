package com.ranjan.somiq.core.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

actual class KotlinInitializer(private val context: Context) {
    actual fun init() {
        initKoin {
            androidContext(context)
            androidLogger()
            modules(androidModules)
        }
    }
}