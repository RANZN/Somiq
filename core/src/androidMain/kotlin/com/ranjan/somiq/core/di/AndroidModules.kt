package com.ranjan.somiq.core.di

import android.content.Context
import androidx.room.RoomDatabase
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getDatabaseBuilder
import org.koin.dsl.module

val androidModules = module {

    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(get<Context>().applicationContext)
    }

    single<ChuckerInterceptor> {
        ChuckerInterceptor.Builder(get<Context>()).build()
    }
}