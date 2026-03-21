package com.ranjan.somiq.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val TOKEN_DATA_STORE_NAME = "somiq_tokens.preferences_pb"

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = TOKEN_DATA_STORE_NAME,
)

private object TokenDataStoreFactory : KoinComponent {
    private val context: Context by inject()
    fun create(scope: CoroutineScope): DataStore<Preferences> = context.tokenDataStore
}

actual fun createTokenStorage(): TokenStorage =
    TokenStorageImpl(TokenDataStoreFactory.create(GlobalScope))
