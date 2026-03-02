package com.ranjan.somiq.core.data.local

interface AuthStateManager {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(isLoggedIn: Boolean)
}

expect class AuthStateManagerImpl() : AuthStateManager
