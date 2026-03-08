package com.ranjan.somiq.core.di

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import org.koin.compose.koinInject

@OptIn(ExperimentalCoilApi::class)
@Composable
fun InitializeCoil() {
    val httpClient: HttpClient = koinInject()

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient))
            }
            .build()
    }
}
