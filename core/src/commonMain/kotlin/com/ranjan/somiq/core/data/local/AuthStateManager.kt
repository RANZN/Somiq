package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface AuthStateManager {
    val userId: Flow<String?>

    suspend fun getUserId(): String? = userId.first()

    suspend fun setUserId(userId: String)

    suspend fun clearUserId()

    suspend fun isLoggedIn(): Boolean = getUserId() != null
}

private val KEY_USER_ID = stringPreferencesKey("user_id")

class AuthStateManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : AuthStateManager {

    override val userId: Flow<String?> = dataStore.data
        .map { it[KEY_USER_ID] }.distinctUntilChanged()

    override suspend fun setUserId(userId: String) {
        dataStore.edit { it[KEY_USER_ID] = userId }
    }

    override suspend fun clearUserId() {
        dataStore.edit { it.remove(KEY_USER_ID) }
    }
}
