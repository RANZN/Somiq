package com.ranjan.somiq.auth.data.model

import com.ranjan.somiq.core.data.model.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val phone: String,
    val otp: String,
    val deviceId: String,
)

@Serializable
enum class OtpVerifyStatusDto {
    LOGGED_IN,
    SIGNUP_REQUIRED,
}

@Serializable
data class VerifyOtpResponse(
    val status: OtpVerifyStatusDto,
    val token: AuthToken? = null,
    val user: User? = null,
    val signupToken: String? = null,
)

@Serializable
data class CompleteSignupRequest(
    val name: String,
    val userId: String,
    val email: String? = null,
    val profilePictureUrl: String? = null,
)

@Serializable
data class CheckUserIdRequest(
    val userId: String,
)

@Serializable
data class CheckUserIdResponse(
    val available: Boolean,
)
