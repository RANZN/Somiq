package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TokenStorage {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getOrCreateDeviceId(): String
    suspend fun setTokens(accessToken: String, refreshToken: String)
    suspend fun clear()
}

private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
private val KEY_DEVICE_ID = stringPreferencesKey("device_id")

class TokenStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenStorage {

    override suspend fun getAccessToken(): String? =
        dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()

    override suspend fun getRefreshToken(): String? =
        dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getOrCreateDeviceId(): String {
        val existing = dataStore.data.map { it[KEY_DEVICE_ID] }.first()
        if (!existing.isNullOrBlank()) return existing
        val generated = Uuid.random().toString()
        dataStore.edit { it[KEY_DEVICE_ID] = generated }
        return generated
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