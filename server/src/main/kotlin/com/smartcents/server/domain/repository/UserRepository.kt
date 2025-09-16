package com.smartcents.server.domain.repository

import com.smartcents.server.domain.model.User

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun isEmailExists(email: String): Boolean
    suspend fun saveUser(user: User): User?
}