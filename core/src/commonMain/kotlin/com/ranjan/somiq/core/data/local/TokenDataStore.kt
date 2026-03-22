package com.ranjan.somiq.core.data.local

/**
 * Returns the TokenStorage instance. All targets use [TokenStorageImpl] backed by
 * Preferences DataStore (file on JVM/iOS, [preferencesDataStore] on Android).
 */
expect fun createTokenStorage(): TokenStorage
