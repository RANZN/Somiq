package com.ranjan.somiq.common.checkForUpdate

interface CheckForUpdateRepository {
    suspend fun isUpdateNeeded() : Result<Boolean>
}