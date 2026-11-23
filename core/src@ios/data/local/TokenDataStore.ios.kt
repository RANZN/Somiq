@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val TOKEN_DATA_STORE_NAME = "somiq_tokens.preferences_pb"

private fun tokenDataStoreFilePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path) + "/" + TOKEN_DATA_STORE_NAME
}
private val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

private val tokenDataStore by lazy {
    PreferenceDataStoreFactory.createWithPath(
        scope = dataStoreScope,
        produceFile = { tokenDataStoreFilePath().toPath() },
    )
}

actual fun createTokenDataStore(): DataStore<Preferences> = tokenDataStore
