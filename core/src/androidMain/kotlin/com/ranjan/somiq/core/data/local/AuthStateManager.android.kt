package com.ranjan.somiq.core.data.local

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AuthStateManagerImpl : AuthStateManager, KoinComponent {
    private val context: Context by inject()
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth_state_prefs", Context.MODE_PRIVATE)
    }
    
    private companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    override fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    override fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .commit()
    }
}
