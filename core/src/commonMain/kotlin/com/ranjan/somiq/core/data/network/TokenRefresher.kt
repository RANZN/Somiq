package com.ranjan.somiq.core.data.network

import com.ranjan.somiq.core.consts.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
private data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
private data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)

interface TokenRefresher {
    suspend fun tryRefresh(oldRefreshToken: String?): TokenPair?
}

class TokenRefresherImpl(
    private val tokenProvider: TokenProvider,
    private val nonAuthHttpClient: HttpClient
) : TokenRefresher {
    private val mutex = Mutex()
    
    override suspend fun tryRefresh(oldRefreshToken: String?): TokenPair? {
        return mutex.withLock {
            val currentAccessToken = tokenProvider.getAccessToken()
            val currentRefreshToken = tokenProvider.getRefreshToken()

            // If tokens have already been refreshed by another coroutine
            if (currentRefreshToken != oldRefreshToken && currentAccessToken != null && currentRefreshToken != null) {
                return@withLock TokenPair(currentAccessToken, currentRefreshToken)
            }

            // If no refresh token available, cannot refresh
            val refreshToken = currentRefreshToken ?: return@withLock null

            try {
                // Make API call to refresh token using non-auth client to avoid circular dependency
                val response = nonAuthHttpClient.post("$BASE_URL/auth/refresh") {
                    setBody(RefreshTokenRequest(refreshToken))
                }

                if (response.status == HttpStatusCode.OK) {
                    val tokenResponse = response.body<RefreshTokenResponse>()
                    
                    // Save new tokens
                    tokenProvider.saveToken(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    
                    return@withLock TokenPair(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                } else {
                    // Refresh failed, clear tokens
                    tokenProvider.clearToken()
                    return@withLock null
                }
            } catch (_: Exception) {
                // Refresh failed, clear tokens
                tokenProvider.clearToken()
                return@withLock null
            }
        }
    }
}