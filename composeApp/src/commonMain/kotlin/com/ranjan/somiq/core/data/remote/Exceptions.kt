package com.ranjan.somiq.core.data.remote

sealed class ApiException(message: String?) : Exception(message) {
    class Unauthorized : ApiException("Invalid credentials")
    class NotFound : ApiException("Resource not found")
    data class ServerError(val code: Int) : ApiException("Server error $code")
}

sealed class NetworkException(message: String?) : Exception(message) {
    class NoNetwork : NetworkException("No internet connection")
    class Timeout : NetworkException("Request timed out")
    data class Generic(val error: String?) : NetworkException(error)
    data class Unknown(val error: String?) : NetworkException(error)
}