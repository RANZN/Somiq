package com.ranjan.somiq.core.data.local

import java.io.File
import java.util.Properties

class AuthStateManagerImpl : AuthStateManager {
    private val stateFile = File(System.getProperty("user.home"), ".somiq_auth_state.properties")
    private val properties = Properties()
    
    init {
        if (stateFile.exists()) {
            stateFile.inputStream().use { properties.load(it) }
        }
    }
    
    private fun saveProperties() {
        stateFile.parentFile?.mkdirs()
        stateFile.outputStream().use { properties.store(it, "Somiq Auth State") }
    }
    
    override fun isLoggedIn(): Boolean {
        return properties.getProperty("is_logged_in", "false").toBoolean()
    }
    
    override fun setLoggedIn(isLoggedIn: Boolean) {
        properties.setProperty("is_logged_in", isLoggedIn.toString())
        saveProperties()
    }
}
