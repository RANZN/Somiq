package com.ranjan.somiq.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

actual fun createHttpClient(shared: HttpClientConfig<*>.() -> Unit): HttpClient {
    TODO("Not yet implemented")
}