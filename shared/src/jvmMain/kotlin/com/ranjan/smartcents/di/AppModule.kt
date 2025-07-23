package com.ranjan.smartcents.di

import androidx.room.RoomDatabase
import com.ranjan.smartcents.data.db.AppDatabase
import com.ranjan.smartcents.util.getDatabaseBuilder
import org.koin.dsl.module

val appModules = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}