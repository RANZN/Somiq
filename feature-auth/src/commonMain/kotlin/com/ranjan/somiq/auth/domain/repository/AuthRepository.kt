package com.ranjan.somiq.auth.domain.repository

import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.model.VerifyOtpResult

interface AuthRepository {
    suspend fun verifyOtp(phone: String, otp: String): VerifyOtpResult

    suspend fun completeSignup(
        signupToken: String,
        name: String,
        userId: String,
        email: String?,
        profilePictureUrl: String?,
    ): AuthResult

    suspend fun checkUserIdAvailable(userId: String): Result<Boolean>
    suspend fun logoutUser(): Boolean
    suspend fun isUserLoggedIn(): Boolean
}
