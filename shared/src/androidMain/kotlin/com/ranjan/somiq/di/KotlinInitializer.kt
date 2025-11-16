package com.ranjan.somiq.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.ranjan.somiq.util.firebaseOptions
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

actual class KotlinInitializer(private val context: Context) {
    actual fun init() {
        FirebaseApp.initializeApp(context, firebaseOptions)
        initKoin {
            androidContext(context)
            androidLogger()
            modules(appModule)
        }
    }
}