package com.ranjan.somiq.di

import androidx.room.RoomDatabase
import com.ranjan.somiq.data.db.AppDatabase
import com.ranjan.somiq.data.db.getRoomDatabase
import com.ranjan.somiq.data.db.platformDatabaseModule
import org.koin.dsl.module

val localDatabaseModule = module {
    includes(platformDatabaseModule())
    single<AppDatabase> {
        getRoomDatabase(get<RoomDatabase.Builder<AppDatabase>>())
    }
}