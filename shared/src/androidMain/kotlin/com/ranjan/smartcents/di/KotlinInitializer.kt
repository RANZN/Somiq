package com.ranjan.smartcents.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

actual class KotlinInitializer(private val context: Context) {
    actual fun init() {
        initKoin {
            androidContext(context)
            androidLogger()
            modules(appModule)
        }
    }
}