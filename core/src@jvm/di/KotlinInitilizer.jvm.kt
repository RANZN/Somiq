package com.ranjan.somiq.core.di

import com.ranjan.somiq.core.platform.JvmMediaPicker
import com.ranjan.somiq.core.platform.MediaPicker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModules: Module
    get() = module {
        single<MediaPicker> { JvmMediaPicker() }
    }