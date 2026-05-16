package com.ranjan.somiq.di

import androidx.room.RoomDatabase
import com.ranjan.somiq.data.db.AppDatabase
import com.ranjan.somiq.data.db.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}
