package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Persistent storage for auth tokens so they survive app restarts.
 * Uses DataStore (KMP) — single implementation in commonMain.
 */
interface TokenStorage {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun setTokens(accessToken: String, refreshToken: String)
    fun clear()
}

private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

/**
 * DataStore-backed token storage. Reads are served from an in-memory cache (loaded at init);
 * writes update cache and persist asynchronously so the API stays synchronous for callers.
 */
class TokenStorageImpl(
    dataStore: DataStore<Preferences>,
) : TokenStorage {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var accessToken: String? = runBlocking { dataStore.data.first().let { it[KEY_ACCESS_TOKEN]?.takeIf { s -> s.isNotEmpty() } } }

    private var refreshToken: String? = runBlocking { dataStore.data.first().let { it[KEY_REFRESH_TOKEN]?.takeIf { s -> s.isNotEmpty() } } }

    private val store = dataStore

    override fun getAccessToken(): String? = accessToken

    override fun getRefreshToken(): String? = refreshToken

    override fun setTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        scope.launch {
            store.edit { prefs ->
                prefs[KEY_ACCESS_TOKEN] = accessToken
                prefs[KEY_REFRESH_TOKEN] = refreshToken
            }
        }
    }

    override fun clear() {
        accessToken = null
        refreshToken = null
        scope.launch {
            store.edit { it.clear() }
        }
    }
}
