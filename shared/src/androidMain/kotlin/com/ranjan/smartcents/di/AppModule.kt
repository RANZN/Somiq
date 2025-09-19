package com.ranjan.smartcents.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ranjan.smartcents.data.db.AppDatabase
import org.koin.dsl.module

val appModule = module {

    single<RoomDatabase.Builder<AppDatabase>> {
        val appContext = get<Context>().applicationContext
        val dbFile = appContext.getDatabasePath("user_database.db")

        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }

    single<ChuckerInterceptor> {
        ChuckerInterceptor.Builder(get<Context>()).build()
    }

}