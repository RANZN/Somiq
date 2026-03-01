package com.ranjan.somiq

import android.app.Application
import com.ranjan.somiq.core.di.KotlinInitializer
import com.ranjan.somiq.di.getAllAppModules

class SomiqApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KotlinInitializer(this).init(
            additionalModules = getAllAppModules()
        )
    }
}