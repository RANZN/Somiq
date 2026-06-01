package com.ranjan.somiq.di

import com.ranjan.somiq.app.home.di.homeModule
import com.ranjan.somiq.app.postDetail.di.postDetailModule
import com.ranjan.somiq.app.search.di.searchModule
import com.ranjan.somiq.auth.di.authModule
import com.ranjan.somiq.chat.di.chatModule
import com.ranjan.somiq.core.di.networkModule
import com.ranjan.somiq.createpost.di.createPostViewModelModule
import com.ranjan.somiq.createstory.di.createStoryViewModelModule
import com.ranjan.somiq.feed.di.feedModule
import com.ranjan.somiq.profile.di.profileModule
import com.ranjan.somiq.reels.di.reelsModule
import org.koin.core.module.Module

val sharedModules
    get(): List<Module> = listOf(
        networkModule,
        localDatabaseModule,
        appDataModule,
        appViewModelModule,
        authModule,
        feedModule,
        profileModule,
        chatModule,
        homeModule,
        searchModule,
        postDetailModule,
        reelsModule,
        createPostViewModelModule,
        createStoryViewModelModule,
        collectionsModule,
        appUserViewModelModule,
    )