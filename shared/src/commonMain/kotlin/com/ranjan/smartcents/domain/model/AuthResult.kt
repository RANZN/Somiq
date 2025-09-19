package com.ranjan.smartcents.domain.model

import com.ranjan.smartcents.data.model.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    sealed class Failure : AuthResult() {
        object EmailAlreadyInUse : Failure()
        object InvalidCredentials : Failure()
        object UserNotFound : Failure()
        object ServerError : Failure()
        object NoNetwork : Failure()
        data class Unknown(val message: String?) : Failure()
    }
}