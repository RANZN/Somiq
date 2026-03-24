package com.ranjan.somiq.auth.data.repository

import com.ranjan.somiq.auth.data.model.AuthResponse
import com.ranjan.somiq.auth.data.model.CheckUserIdRequest
import com.ranjan.somiq.auth.data.model.CheckUserIdResponse
import com.ranjan.somiq.auth.data.model.CompleteSignupRequest
import com.ranjan.somiq.auth.data.model.ErrorResponse
import com.ranjan.somiq.auth.data.model.OtpVerifyStatusDto
import com.ranjan.somiq.auth.data.model.VerifyOtpRequest
import com.ranjan.somiq.auth.data.model.VerifyOtpResponse
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.model.VerifyOtpResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.local.DeviceIdProvider
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.data.network.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val nonAuthHttpClient: HttpClient,
    private val authHttpClient: HttpClient,
    private val tokenProvider: TokenProvider,
    private val authStateManager: AuthStateManager,
    private val deviceIdProvider: DeviceIdProvider,
) : AuthRepository {

    override suspend fun verifyOtp(phone: String, otp: String): VerifyOtpResult {
        return try {
            val deviceId = deviceIdProvider.getDeviceId()
            val response = nonAuthHttpClient.post("$BASE_URL/auth/verify-otp") {
                setBody(
                    VerifyOtpRequest(
                        phone = phone,
                        otp = otp,
                        deviceId = deviceId,
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.body<VerifyOtpResponse>()
                    when (body.status) {
                        OtpVerifyStatusDto.LOGGED_IN -> {
                            val token = body.token ?: return VerifyOtpResult.Failure.ServerError
                            val user = body.user ?: return VerifyOtpResult.Failure.ServerError
                            tokenProvider.saveToken(
                                accessToken = token.accessToken,
                                refreshToken = token.refreshToken
                            )
                            authStateManager.setLoggedIn(true)
                            VerifyOtpResult.LoggedIn(user)
                        }

                        OtpVerifyStatusDto.SIGNUP_REQUIRED -> {
                            val st = body.signupToken ?: return VerifyOtpResult.Failure.ServerError
                            VerifyOtpResult.SignupRequired(st)
                        }
                    }
                }

                HttpStatusCode.Unauthorized -> {
                    val msg = try {
                        response.body<ErrorResponse>().message
                    } catch (_: Exception) {
                        null
                    }
                    when (msg) {
                        "ACCOUNT_NOT_FOUND" -> VerifyOtpResult.Failure.AccountNotFound
                        "INVALID_OTP" -> VerifyOtpResult.Failure.InvalidOtp
                        else -> VerifyOtpResult.Failure.InvalidOtp
                    }
                }

                HttpStatusCode.NotFound -> VerifyOtpResult.Failure.AccountNotFound

                HttpStatusCode.Conflict -> {
                    val msg = try {
                        response.body<ErrorResponse>().message
                    } catch (_: Exception) {
                        null
                    }
                    if (msg == "PHONE_ALREADY_REGISTERED") {
                        VerifyOtpResult.Failure.PhoneAlreadyRegistered
                    } else {
                        VerifyOtpResult.Failure.ServerError
                    }
                }

                else -> VerifyOtpResult.Failure.ServerError
            }
        } catch (_: NetworkException.NoNetwork) {
            VerifyOtpResult.Failure.NoNetwork
        } catch (e: Exception) {
            VerifyOtpResult.Failure.Unknown(e.message)
        }
    }

    override suspend fun completeSignup(
        signupToken: String,
        name: String,
        userId: String,
        email: String?,
        profilePictureUrl: String?,
    ): AuthResult {
        return try {
            val response = nonAuthHttpClient.post("$BASE_URL/auth/complete-signup") {
                header(HttpHeaders.Authorization, "Bearer $signupToken")
                setBody(
                    CompleteSignupRequest(
                        name = name,
                        userId = userId,
                        email = email?.takeIf { it.isNotBlank() },
                        profilePictureUrl = profilePictureUrl?.takeIf { it.isNotBlank() },
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val authResponse = response.body<AuthResponse>()
                    tokenProvider.saveToken(
                        accessToken = authResponse.token.accessToken,
                        refreshToken = authResponse.token.refreshToken
                    )
                    authStateManager.setLoggedIn(true)
                    AuthResult.Success(authResponse.user)
                }

                HttpStatusCode.Conflict -> {
                    val errorResponse = try {
                        response.body<ErrorResponse>()
                    } catch (_: Exception) {
                        null
                    }
                    when (errorResponse?.message) {
                        "USERNAME_ALREADY_IN_USE" -> AuthResult.Failure.UsernameAlreadyInUse
                        "PHONE_ALREADY_IN_USE" -> AuthResult.Failure.PhoneAlreadyInUse
                        "EMAIL_ALREADY_IN_USE" -> AuthResult.Failure.EmailAlreadyInUse
                        else -> AuthResult.Failure.ServerError
                    }
                }
                else -> AuthResult.Failure.ServerError
            }
        } catch (_: NetworkException.NoNetwork) {
            AuthResult.Failure.NoNetwork
        } catch (e: Exception) {
            AuthResult.Failure.Unknown(e.message)
        }
    }

    override suspend fun checkUserIdAvailable(userId: String): Result<Boolean> = runCatching {
        val response = nonAuthHttpClient.post("$BASE_URL/auth/check-user-id") {
            setBody(CheckUserIdRequest(userId))
        }
        require(response.status == HttpStatusCode.OK) { "Server error" }
        response.body<CheckUserIdResponse>().available
    }

    override suspend fun logoutUser(): Boolean {
        return try {
            val refreshToken = tokenProvider.getRefreshToken()

            tokenProvider.clearToken()
            authStateManager.setLoggedIn(false)

            if (refreshToken != null) {
                try {
                    authHttpClient.post("$BASE_URL/auth/logout") {
                        header(HttpHeaders.Authorization, "Bearer $refreshToken")
                    }
                } catch (_: Exception) {
                }
            }

            true
        } catch (_: Exception) {
            tokenProvider.clearToken()
            authStateManager.setLoggedIn(false)
            true
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val isLoggedInState = authStateManager.isLoggedIn()
        val hasToken = tokenProvider.getAccessToken() != null || tokenProvider.getRefreshToken() != null

        if (isLoggedInState && !hasToken) {
            authStateManager.setLoggedIn(false)
            return false
        }

        return isLoggedInState && hasToken
    }
}
