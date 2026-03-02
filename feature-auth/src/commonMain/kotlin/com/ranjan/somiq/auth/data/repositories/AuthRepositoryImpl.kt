package com.ranjan.somiq.auth.data.repositories

import com.ranjan.somiq.auth.data.model.AuthResponse
import com.ranjan.somiq.auth.data.model.ErrorResponse
import com.ranjan.somiq.auth.data.model.LoginRequest
import com.ranjan.somiq.auth.data.model.SignUpRequest
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.koin.core.qualifier.named
import com.ranjan.somiq.core.di.NonAuthClient

class AuthRepositoryImpl(
    private val nonAuthHttpClient: HttpClient,
    private val authHttpClient: HttpClient,
    private val tokenProvider: TokenProvider,
    private val authStateManager: AuthStateManager,
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return try {
            val response = nonAuthHttpClient.post("$BASE_URL/auth/login") {
                setBody(
                    LoginRequest(
                        email,
                        password
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    tokenProvider.saveToken(
                        accessToken = authResponse.token.accessToken,
                        refreshToken = authResponse.token.refreshToken
                    )
                    authStateManager.setLoggedIn(true)
                    AuthResult.Success(authResponse.user)
                }

                HttpStatusCode.Unauthorized -> AuthResult.Failure.InvalidCredentials
                HttpStatusCode.NotFound -> AuthResult.Failure.UserNotFound
                else -> AuthResult.Failure.ServerError
            }
        } catch (_: NetworkException.NoNetwork) {
            AuthResult.Failure.NoNetwork
        } catch (e: Exception) {
            AuthResult.Failure.Unknown(e.message)
        }
    }

    override suspend fun signUpUser(name: String, username: String, email: String, password: String): AuthResult {
        return try {
            val response = nonAuthHttpClient.post("$BASE_URL/auth/signup") {
                setBody(
                    SignUpRequest(
                        name = name,
                        username = username,
                        email = email,
                        password = password
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
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
                    } catch (e: Exception) {
                        null
                    }
                    when (errorResponse?.message) {
                        "USERNAME_ALREADY_IN_USE" -> AuthResult.Failure.UsernameAlreadyInUse
                        "EMAIL_ALREADY_IN_USE" -> AuthResult.Failure.EmailAlreadyInUse
                        else -> AuthResult.Failure.EmailAlreadyInUse // Default to email for backward compatibility
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
                } catch (e: Exception) {
                    // Ignore network errors
                }
            }
            
            true
        } catch (e: Exception) {
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