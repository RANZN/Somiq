package com.ranjan.somiq.core.data.local

import platform.Foundation.NSUserDefaults

class AuthStateManagerImpl : AuthStateManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    
    private companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    override fun isLoggedIn(): Boolean {
        return userDefaults.boolForKey(KEY_IS_LOGGED_IN)
    }
    
    override fun setLoggedIn(isLoggedIn: Boolean) {
        userDefaults.setBool(isLoggedIn, forKey = KEY_IS_LOGGED_IN)
        userDefaults.synchronize()
    }
}
