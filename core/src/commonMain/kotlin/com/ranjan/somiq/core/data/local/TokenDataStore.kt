package com.ranjan.somiq.core.data.local

/**
 * Returns the TokenStorage instance. One implementation in commonMain (DataStore-based)
 * for Android/JVM; iOS can use platform storage (UserDefaults) without DataStore.
 */
expect fun createTokenStorage(): TokenStorage
