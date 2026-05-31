package com.ranjan.somiq.profile.di

import com.ranjan.somiq.profile.data.repository.ProfileRepositoryImpl
import com.ranjan.somiq.profile.domain.repository.ProfileRepository
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import com.ranjan.somiq.profile.ui.ProfileViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetProfileUseCase)
    viewModelOf(::ProfileViewModel)
}
