package com.ranjan.somiq.core.data.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import org.koin.mp.KoinPlatform.getKoin


actual fun createHttpClient(shared: HttpClientConfig<*>.() -> Unit): HttpClient {

    val chuckerInterceptor = getKoin().get<ChuckerInterceptor>()

    return HttpClient(OkHttp) {
        engine {
            preconfigured = OkHttpClient.Builder()
                .addInterceptor(chuckerInterceptor)
                .build()
        }
        shared()
    }
}