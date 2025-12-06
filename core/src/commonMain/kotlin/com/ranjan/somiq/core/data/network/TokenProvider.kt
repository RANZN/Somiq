package com.ranjan.somiq.core.data.network

import kotlin.concurrent.Volatile

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveToken(accessToken: String, refreshToken: String)
    fun clearToken()
}

/**
 * In-memory implementation of TokenProvider.
 * 
 * Note: This is a basic implementation that stores tokens in memory.
 * For production use, consider implementing a platform-specific version
 * that uses secure storage (e.g., EncryptedSharedPreferences on Android,
 * Keychain on iOS, or secure storage on other platforms).
 * 
 * This implementation can be overridden in platform-specific modules.
 */
class TokenProviderImpl : TokenProvider {
    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var refreshToken: String? = null

    override fun getAccessToken(): String? {
        return accessToken
    }

    override fun getRefreshToken(): String? {
        return refreshToken
    }

    override fun saveToken(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun clearToken() {
        accessToken = null
        refreshToken = null
    }
}