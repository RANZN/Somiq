package com.ranjan.somiq.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("user_database.db")
    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath,
    )
}

actual fun platformDatabaseModule(): Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(get<Context>().applicationContext)
    }
}
