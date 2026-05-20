package com.ranjan.somiq.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createTokenDataStore(): DataStore<Preferences>
