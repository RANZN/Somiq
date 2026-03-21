package com.ranjan.somiq.core.data.network

import com.ranjan.somiq.core.data.local.TokenStorage

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveToken(accessToken: String, refreshToken: String)
    suspend fun clearToken()
}

class TokenProviderImpl(
    private val storage: TokenStorage,
) : TokenProvider {

    override suspend fun getAccessToken(): String? = storage.getAccessToken()

    override suspend fun getRefreshToken(): String? = storage.getRefreshToken()

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        storage.setTokens(accessToken, refreshToken)
    }

    override suspend fun clearToken() {
        storage.clear()
    }
}