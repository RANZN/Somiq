package com.ranjan.somiq.core.di

import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepository
import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepositoryImpl
import com.ranjan.somiq.common.checkForUpdate.CheckUpdateUseCase
import androidx.room.RoomDatabase
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getRoomDatabase
import com.ranjan.somiq.core.data.repository.CollectionRepositoryImpl
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import com.ranjan.somiq.core.domain.usecase.GetCollectionsUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single<AppDatabase> {
        getRoomDatabase(get<RoomDatabase.Builder<AppDatabase>>())
    }

    factoryOf(::CheckForUpdateRepositoryImpl) bind CheckForUpdateRepository::class
    factoryOf(::CheckUpdateUseCase)
    
    factory<CollectionRepository> {
        CollectionRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetCollectionsUseCase)
}