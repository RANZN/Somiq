package com.ranjan.somiq

import android.app.Application
import com.ranjan.somiq.auth.di.authModule
import com.ranjan.somiq.auth.di.authViewModelModule
import com.ranjan.somiq.core.di.KotlinInitializer
import com.ranjan.somiq.di.appViewModelModule
import com.ranjan.somiq.home.di.homeModule
import com.ranjan.somiq.home.di.homeViewModelModule

class SomiqApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KotlinInitializer(this).init(
            additionalModules = listOf(
                authModule,
                authViewModelModule,
                homeModule,
                homeViewModelModule,
                appViewModelModule
            )
        )
    }

}