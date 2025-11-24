package com.ranjan.somiq

import android.app.Application
import com.ranjan.somiq.core.di.KotlinInitializer

class SomiqApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KotlinInitializer(this).init()
    }

}