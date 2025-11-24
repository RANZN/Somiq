package com.ranjan.somiq.core.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getRoomDatabaseBuilder
import org.koin.dsl.module

val androidModules = module {

    single<AppDatabase> {
        val appContext = get<Context>().applicationContext
        val dbFile = appContext.getDatabasePath("user_database.db")
        val builder = Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
        getRoomDatabaseBuilder(builder)
    }

    single<ChuckerInterceptor> {
        ChuckerInterceptor.Builder(get<Context>()).build()
    }
}