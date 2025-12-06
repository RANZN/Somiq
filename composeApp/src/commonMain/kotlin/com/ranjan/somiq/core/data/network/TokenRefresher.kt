package com.ranjan.somiq.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface TokenRefresher {
    suspend fun tryRefresh(oldRefreshToken: String?): Boolean
}

class TokenRefresherImpl(
    private val tokenProvider: TokenProvider,
) : TokenRefresher {
    val mutex = Mutex()
    override suspend fun tryRefresh(oldRefreshToken: String?): Boolean {
        return mutex.withLock {
            val accessToken = tokenProvider.getAccessToken()
            val refreshToken = tokenProvider.getRefreshToken()

            if (refreshToken != oldRefreshToken && accessToken != null) {
                return@withLock true
            }
            //do API call

            return@withLock false
        }
    }

}

private class AuthRequestRunner(
    private val client: HttpClient,
    private val tokenProvider: TokenProvider,
    private val tokenRefresher: TokenRefresher
) {
    private val refreshMutex = Mutex()

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend inline fun <reified T> runRequest(crossinline builder: HttpRequestBuilder.() -> Unit): T {
        try {
            return client.request {
                tokenProvider.getAccessToken()?.let { header("Authorization", "Bearer $it") }
                builder()
            }.body()
        } catch (cause: ResponseException) {
            // If not 401 -> rethrow
            val status = cause.response.status
            if (status != HttpStatusCode.Unauthorized) throw cause

            val currentToken = tokenProvider.getRefreshToken()
            val refreshed = tokenRefresher.tryRefresh(currentToken)

            if (!refreshed) {
                tokenProvider.clearToken()
                throw ApiException.Unauthorized() // or throw your SessionExpiredException
            }

            // retry once with fresh token
            val newToken = tokenProvider.getAccessToken()
            if (newToken == null) {
                tokenProvider.clearTokens()
                throw ApiException.Unauthorized()
            }

            return try {
                client.request {
                    header("Authorization", "Bearer $newToken")
                    builder()
                }.body()
            } catch (e: ResponseException) {
                // If retry still fails, bubble up (or you can convert to Unauthorized)
                throw e
            }
        } catch (io: Throwable) {
            // other exceptions, bubble up
            throw io
        }
    }
}