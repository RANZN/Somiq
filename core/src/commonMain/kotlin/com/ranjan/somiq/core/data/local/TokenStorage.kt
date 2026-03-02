package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface TokenStorage {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setTokens(accessToken: String, refreshToken: String)
    suspend fun clear()
}

private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

class TokenStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenStorage {

    private var cacheAccessToken: String? = null
    override suspend fun getAccessToken(): String? {
        if (cacheAccessToken != null) return cacheAccessToken
        cacheAccessToken = dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()
        return cacheAccessToken
    }

    private var cacheRefreshToken: String? = null
    override suspend fun getRefreshToken(): String? {
        if (cacheRefreshToken != null) return cacheRefreshToken
        cacheRefreshToken = dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()
        return cacheRefreshToken
    }

    override suspend fun setTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[KEY_ACCESS_TOKEN] = accessToken
            it[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}