package com.ranjan.somiq.core.di

import androidx.room.RoomDatabase
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getDatabaseBuilder
import org.koin.dsl.module

val iosModules = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}
