package com.ranjan.somiq.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ranjan.somiq.core.data.db.dao.UserDao
import com.ranjan.somiq.core.data.db.entity.User
import kotlinx.coroutines.Dispatchers

@Database(entities = [User::class], version = 1, exportSchema = false)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(builder: Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .build()
}