package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import java.io.File

private val tokenDataStoreFile = File(System.getProperty("user.home"), ".somiq_tokens.preferences_pb")

private object TokenDataStoreFactory {
    fun create(scope: CoroutineScope): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { tokenDataStoreFile },
        )
}

actual fun createTokenStorage(): TokenStorage =
    TokenStorageImpl(TokenDataStoreFactory.create(GlobalScope))
