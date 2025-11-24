package com.ranjan.somiq.auth.data.repositories

import com.ranjan.somiq.auth.data.model.AuthResponse
import com.ranjan.somiq.auth.data.model.LoginRequest
import com.ranjan.somiq.auth.data.model.SignUpRequest
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.remote.NetworkException
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return try {
            val response = httpClient.post("$BASE_URL/auth/login") {
                setBody(LoginRequest(email, password))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
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

    override suspend fun signUpUser(name: String, email: String, password: String): AuthResult {
        return try {
            val response = httpClient.post("$BASE_URL/auth/signup") {
                setBody(SignUpRequest(name, email, password))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    AuthResult.Success(authResponse.user)
                }

                HttpStatusCode.Conflict -> AuthResult.Failure.EmailAlreadyInUse
                else -> AuthResult.Failure.ServerError
            }
        } catch (_: NetworkException.NoNetwork) {
            AuthResult.Failure.NoNetwork
        } catch (e: Exception) {
            AuthResult.Failure.Unknown(e.message)
        }
    }

    override suspend fun logoutUser(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

}