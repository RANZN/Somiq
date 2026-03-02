package com.ranjan.somiq.core.data.local

import platform.Foundation.NSUserDefaults

/**
 * iOS: TokenStorage using UserDefaults (no DataStore dependency on iOS).
 * Single createTokenStorage() entry point; storage logic in one place per platform.
 */
actual fun createTokenStorage(): TokenStorage = TokenStorageImpl()

private class TokenStorageImpl : TokenStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override suspend fun getAccessToken(): String? =
        userDefaults.stringForKey(KEY_ACCESS_TOKEN)?.takeIf { it.isNotEmpty() }

    override suspend fun getRefreshToken(): String? =
        userDefaults.stringForKey(KEY_REFRESH_TOKEN)?.takeIf { it.isNotEmpty() }

    override suspend fun setTokens(accessToken: String, refreshToken: String) {
        userDefaults.setObject(accessToken, forKey = KEY_ACCESS_TOKEN)
        userDefaults.setObject(refreshToken, forKey = KEY_REFRESH_TOKEN)
        userDefaults.synchronize()
    }

    override suspend fun clear() {
        userDefaults.removeObjectForKey(KEY_ACCESS_TOKEN)
        userDefaults.removeObjectForKey(KEY_REFRESH_TOKEN)
        userDefaults.synchronize()
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
