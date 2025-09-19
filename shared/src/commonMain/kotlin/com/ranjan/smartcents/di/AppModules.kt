package com.ranjan.smartcents.di

import com.ranjan.smartcents.data.db.AppDatabase
import com.ranjan.smartcents.data.db.getRoomDatabaseBuilder
import com.ranjan.smartcents.data.remote.createHttpClient
import com.ranjan.smartcents.data.remote.setupCommonPlugins
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