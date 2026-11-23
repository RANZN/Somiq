package com.ranjan.somiq.db

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "user_database.db")
    return Room.databaseBuilder(
        name = dbFile.absolutePath,
    )
}

actual fun platformDatabaseModule(): Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}
