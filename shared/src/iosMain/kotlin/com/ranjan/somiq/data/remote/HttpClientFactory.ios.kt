package com.ranjan.somiq.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.*

actual fun createHttpClient(shared: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(Darwin) {
        shared()
    }
}