package com.ranjan.smartcents.domain.repository

interface AppRepository {
    suspend fun isUpdateNeeded() : Result<Boolean>
}