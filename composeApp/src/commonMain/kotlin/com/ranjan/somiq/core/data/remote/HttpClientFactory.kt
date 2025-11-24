package com.ranjan.somiq.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

expect fun createHttpClient(shared: HttpClientConfig<*>.() -> Unit): HttpClient

fun HttpClientConfig<*>.setupCommonPlugins() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
            contentType = ContentType.Application.Json
        )
    }

    defaultRequest {
        contentType(ContentType.Application.Json) // default Content-Type
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    expectSuccess = false

    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            throw when (cause) {
                is UnresolvedAddressException -> NetworkException.NoNetwork()
                is ConnectTimeoutException -> NetworkException.Timeout()
                is SocketTimeoutException -> NetworkException.Timeout()
                is IOException -> NetworkException.Generic(cause.message)
                else -> NetworkException.Unknown(cause.message)
            }
        }

        validateResponse { response ->
            if (response.status.value >= 400) {
                throw when (response.status) {
                    HttpStatusCode.Unauthorized -> ApiException.Unauthorized()
                    HttpStatusCode.NotFound -> ApiException.NotFound()
                    else -> ApiException.ServerError(response.status.value)
                }
            }
        }
    }
}