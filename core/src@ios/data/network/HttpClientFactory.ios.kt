package com.ranjan.somiq.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClient(shared: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(Darwin) {
        shared()
    }
}

