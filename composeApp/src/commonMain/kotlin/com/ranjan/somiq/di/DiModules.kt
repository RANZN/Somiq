package com.ranjan.somiq.di

import com.ranjan.somiq.auth.di.authModule
import com.ranjan.somiq.auth.di.authViewModelModule
import com.ranjan.somiq.feed.di.feedModule
import com.ranjan.somiq.feed.di.feedViewModelModule
import com.ranjan.somiq.home.di.homeModule
import com.ranjan.somiq.home.di.homeViewModelModule
import com.ranjan.somiq.postDetail.di.postDetailModule
import com.ranjan.somiq.postDetail.di.postDetailViewModelModule
import com.ranjan.somiq.profile.di.profileModule
import com.ranjan.somiq.profile.di.profileViewModelModule
import com.ranjan.somiq.reels.di.reelsModule
import com.ranjan.somiq.reels.di.reelsViewModelModule
import com.ranjan.somiq.search.di.searchModule
import com.ranjan.somiq.search.di.searchViewModelModule
import org.koin.core.module.Module

/**
 * Returns all app modules that need to be initialized for Koin.
 * This centralizes module registration so you only need to update it in one place.
 */
fun getAllAppModules(): List<Module> = listOf(
    authModule,
    authViewModelModule,
    feedModule,
    feedViewModelModule,
    reelsModule,
    reelsViewModelModule,
    profileModule,
    profileViewModelModule,
    searchModule,
    searchViewModelModule,
    postDetailModule,
    postDetailViewModelModule,
    homeModule,
    // homeViewModelModule removed - home module is now just for navigation
    appViewModelModule,
)