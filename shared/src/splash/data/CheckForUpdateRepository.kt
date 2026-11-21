package com.ranjan.somiq.splash.data

interface CheckForUpdateRepository {
    suspend fun isUpdateNeeded() : Result<Boolean>
}