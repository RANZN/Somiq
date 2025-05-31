package com.ranjan.smartcents.domain.model

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    sealed class Failure : AuthResult() {
        object EmailAlreadyInUse : Failure()
        data class Unknown(val message: String?) : Failure()
        companion object {
            fun fromException(e: Exception): Failure {
                val msg = e.message ?: return Unknown(null)
                return when {
                    "EMAIL_EXISTS" in msg -> EmailAlreadyInUse
                    else -> Unknown(msg)
                }
            }
        }
    }
}