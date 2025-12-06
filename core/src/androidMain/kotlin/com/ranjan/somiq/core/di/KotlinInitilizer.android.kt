package com.ranjan.somiq.core.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.module.Module

actual class KotlinInitializer(private val context: Context) {
    actual fun init(additionalModules: List<Module>) {
        initKoin(
            additionalModules = additionalModules,
            appDeclaration = {
                androidContext(context)
                androidLogger()
                modules(androidModules)
            }
        )
    }
}