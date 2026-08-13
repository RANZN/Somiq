package com.ranjan.somiq.core.di

import androidx.compose.runtime.staticCompositionLocalOf
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient

@OptIn(ExperimentalCoilApi::class)
class AppImageLoaderFactory(
    private val httpClient: HttpClient
) {
    fun create(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient))
            }
            .build()
    }
}

val LocalAppImageLoader = staticCompositionLocalOf<ImageLoader> {
    error("No AppImageLoader provided")
}
