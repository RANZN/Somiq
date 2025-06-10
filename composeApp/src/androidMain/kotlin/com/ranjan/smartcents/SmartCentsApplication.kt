package com.ranjan.smartcents

import android.app.Application
import com.ranjan.smartcents.di.KotlinInitializer

class SmartCentsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KotlinInitializer(this).init()
    }
}