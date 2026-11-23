package com.ranjan.somiq.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.mp.KoinPlatform.getKoin

private const val TOKEN_DATA_STORE_NAME = "somiq_tokens.preferences_pb"

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = TOKEN_DATA_STORE_NAME,
)

actual fun createTokenDataStore(): DataStore<Preferences> = getKoin().get<Context>().tokenDataStore
