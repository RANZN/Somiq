package com.ranjan.somiq.di

import com.ranjan.somiq.auth.di.authModule
import com.ranjan.somiq.auth.di.authViewModelModule
import com.ranjan.somiq.feed.di.feedModule
import com.ranjan.somiq.feed.di.feedViewModelModule
import com.ranjan.somiq.app.home.di.homeModule
import com.ranjan.somiq.app.home.di.homeViewModelModule
import com.ranjan.somiq.app.postDetail.di.postDetailModule
import com.ranjan.somiq.app.postDetail.di.postDetailViewModelModule
import com.ranjan.somiq.profile.di.profileModule
import com.ranjan.somiq.profile.di.profileViewModelModule
import com.ranjan.somiq.reels.di.reelsModule
import com.ranjan.somiq.reels.di.reelsViewModelModule
import com.ranjan.somiq.app.search.di.searchModule
import com.ranjan.somiq.app.search.di.searchViewModelModule
import com.ranjan.somiq.chat.di.chatModule
import com.ranjan.somiq.chat.di.chatViewModelModule
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
    homeViewModelModule,
    chatModule,
    chatViewModelModule,
    appViewModelModule,
)