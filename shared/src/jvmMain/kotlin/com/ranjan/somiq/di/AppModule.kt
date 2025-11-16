package com.ranjan.somiq.di

import androidx.room.RoomDatabase
import com.ranjan.somiq.data.db.AppDatabase
import com.ranjan.somiq.util.getDatabaseBuilder
import org.koin.dsl.module

val appModules = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}