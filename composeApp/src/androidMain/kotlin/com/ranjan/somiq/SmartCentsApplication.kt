package com.ranjan.somiq

import android.app.Application
import com.ranjan.somiq.di.KotlinInitializer

class SmartCentsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KotlinInitializer(this).init()
    }
}