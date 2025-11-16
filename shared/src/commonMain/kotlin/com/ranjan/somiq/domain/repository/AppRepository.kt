package com.ranjan.somiq.domain.repository

interface AppRepository {
    suspend fun isUpdateNeeded() : Result<Boolean>
}