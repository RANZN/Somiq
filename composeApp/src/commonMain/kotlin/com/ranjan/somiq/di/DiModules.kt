package com.ranjan.somiq.di

import com.ranjan.somiq.auth.di.authModule
import com.ranjan.somiq.auth.di.authViewModelModule
import com.ranjan.somiq.home.di.homeModule
import com.ranjan.somiq.home.di.homeViewModelModule
import org.koin.core.module.Module

/**
 * Returns all app modules that need to be initialized for Koin.
 * This centralizes module registration so you only need to update it in one place.
 */
fun getAllAppModules(): List<Module> = listOf(
    authModule,
    authViewModelModule,
    homeModule,
    homeViewModelModule,
    appViewModelModule,
)