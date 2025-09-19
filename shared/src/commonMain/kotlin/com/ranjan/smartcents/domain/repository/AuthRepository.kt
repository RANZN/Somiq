package com.ranjan.smartcents.domain.repository

import com.ranjan.smartcents.domain.model.AuthResult

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): AuthResult
    suspend fun signUpUser(name: String, email: String, password: String): AuthResult
    suspend fun logoutUser(): Boolean
    suspend fun isUserLoggedIn(): Boolean
}