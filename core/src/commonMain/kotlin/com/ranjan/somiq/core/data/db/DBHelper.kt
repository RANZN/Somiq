package com.ranjan.somiq.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import com.ranjan.somiq.core.data.db.dao.UserDao
import com.ranjan.somiq.core.data.db.entity.User

// Note: For full KMP support, @ConstructedBy is required for non-Android platforms
// Currently configured for Android only. To enable other platforms:
// 1. Add @ConstructedBy(AppDatabaseConstructor::class)
// 2. Create expect object AppDatabaseConstructor in commonMain
// 3. Provide actual implementations for each platform
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

fun getRoomDatabaseBuilder(
    builder: Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .build()
}