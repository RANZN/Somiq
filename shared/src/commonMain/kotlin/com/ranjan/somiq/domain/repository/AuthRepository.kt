package com.ranjan.somiq.domain.repository

import com.ranjan.somiq.domain.model.AuthResult

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): AuthResult
    suspend fun signUpUser(name: String, email: String, password: String): AuthResult
    suspend fun logoutUser(): Boolean
    suspend fun isUserLoggedIn(): Boolean
}