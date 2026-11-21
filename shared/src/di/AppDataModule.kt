package com.ranjan.somiq.di

import com.ranjan.somiq.splash.data.CheckForUpdateRepository
import com.ranjan.somiq.splash.data.CheckForUpdateRepositoryImpl
import com.ranjan.somiq.splash.data.CheckUpdateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appDataModule = module {
    factoryOf(::CheckForUpdateRepositoryImpl) bind CheckForUpdateRepository::class
    factoryOf(::CheckUpdateUseCase)
}
