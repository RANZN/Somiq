package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File

private val tokenDataStoreFile = File(System.getProperty("user.home"), ".somiq_tokens.preferences_pb")
private val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

private val tokenDataStore by lazy {
    PreferenceDataStoreFactory.create(
        scope = dataStoreScope,
        produceFile = { tokenDataStoreFile }
    )
}

actual fun createTokenDataStore(): DataStore<Preferences> = tokenDataStore
