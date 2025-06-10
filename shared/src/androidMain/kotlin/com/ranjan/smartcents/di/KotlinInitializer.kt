package com.ranjan.smartcents.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

actual class KotlinInitializer(private val context: Context) {
    actual fun init() {
        val options = FirebaseOptions.Builder()
            .setProjectId("we4rule-1f526")
            .setApplicationId("1:477460285859:android:65c6e012419365c238f216") // From google-services.json -> client -> client_info -> mobilesdk_app_id
            .setApiKey("AIzaSyB4q-Tg24TamJBsI6VQLBDuEn5x8jD4WLA") // From google-services.json -> client -> api_key -> current_key
            .build()
        FirebaseApp.initializeApp(context, options)
        initKoin {
            androidContext(context)
            androidLogger()
            modules(appModule)
        }
    }
}