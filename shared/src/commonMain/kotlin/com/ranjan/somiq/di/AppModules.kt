package com.ranjan.somiq.di

import com.ranjan.somiq.data.db.AppDatabase
import com.ranjan.somiq.data.db.getRoomDatabaseBuilder
import com.ranjan.somiq.data.remote.createHttpClient
import com.ranjan.somiq.data.remote.setupCommonPlugins
import io.ktor.client.HttpClient
import org.koin.dsl.module

val appModules = module {

    single<AppDatabase> {
        getRoomDatabaseBuilder(get())
    }

    single<HttpClient> {
        createHttpClient {
            setupCommonPlugins()
        }
    }

}