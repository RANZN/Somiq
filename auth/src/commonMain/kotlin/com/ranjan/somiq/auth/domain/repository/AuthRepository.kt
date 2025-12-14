package com.ranjan.somiq.auth.domain.repository

import com.ranjan.somiq.auth.domain.model.AuthResult

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): AuthResult
    suspend fun signUpUser(name: String, username: String, email: String, password: String): AuthResult
    suspend fun logoutUser(): Boolean
    suspend fun isUserLoggedIn(): Boolean
}