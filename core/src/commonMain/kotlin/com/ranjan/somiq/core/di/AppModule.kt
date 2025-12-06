package com.ranjan.somiq.core.di

import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepository
import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepositoryImpl
import com.ranjan.somiq.common.checkForUpdate.CheckUpdateUseCase
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getRoomDatabaseBuilder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single<AppDatabase> {
        getRoomDatabaseBuilder(get())
    }

    factoryOf(::CheckForUpdateRepositoryImpl) bind CheckForUpdateRepository::class
    factoryOf(::CheckUpdateUseCase)
}