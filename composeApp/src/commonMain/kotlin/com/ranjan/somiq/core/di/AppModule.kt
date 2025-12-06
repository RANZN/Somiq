package com.ranjan.somiq.core.di

import com.ranjan.somiq.auth.data.repositories.AuthRepositoryImpl
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.auth.domain.usecase.LoginUseCase
import com.ranjan.somiq.auth.domain.usecase.SignupUseCase
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepository
import com.ranjan.somiq.common.checkForUpdate.CheckForUpdateRepositoryImpl
import com.ranjan.somiq.common.checkForUpdate.CheckUpdateUseCase
import com.ranjan.somiq.core.data.db.AppDatabase
import com.ranjan.somiq.core.data.db.getRoomDatabaseBuilder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {

    single<AppDatabase> {
        getRoomDatabaseBuilder(get())
    }

    factoryOf(::CheckForUpdateRepositoryImpl) bind CheckForUpdateRepository::class
    factoryOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::CheckUpdateUseCase)
    factoryOf(::UserLoginStatus)
    factoryOf(::SignupUseCase)
    factoryOf(::LoginUseCase)

}