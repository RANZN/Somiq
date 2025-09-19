package com.ranjan.smartcents.data.remote

sealed class ApiException(message: String?) : Exception(message) {
    object Unauthorized : ApiException("Invalid credentials")
    object NotFound : ApiException("Resource not found")
    data class ServerError(val code: Int) : ApiException("Server error $code")
}

sealed class NetworkException(message: String?) : Exception(message) {
    object NoNetwork : NetworkException("No internet connection")
    object Timeout : NetworkException("Request timed out")
    data class Generic(val error: String?) : NetworkException(error)
    data class Unknown(val error: String?) : NetworkException(error)
}