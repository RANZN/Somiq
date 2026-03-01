package com.ranjan.somiq.core.data.network

import com.ranjan.somiq.core.data.local.TokenStorage

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveToken(accessToken: String, refreshToken: String)
    fun clearToken()
}

/**
 * TokenProvider that persists tokens via [TokenStorage] so login state
 * survives app restarts and [isUserLoggedIn] returns true after login/signup.
 */
class TokenProviderImpl(
    private val storage: TokenStorage,
) : TokenProvider {

    override fun getAccessToken(): String? = storage.getAccessToken()

    override fun getRefreshToken(): String? = storage.getRefreshToken()

    override fun saveToken(accessToken: String, refreshToken: String) {
        storage.setTokens(accessToken, refreshToken)
    }

    override fun clearToken() {
        storage.clear()
    }
}