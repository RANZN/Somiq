package com.ranjan.somiq.core.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

/**
 * Wraps an API call with consistent error handling for common scenarios:
 * - Network errors (NoNetwork, Timeout)
 * - API errors (Unauthorized, NotFound, ServerError)
 * - Serialization errors
 * - Generic exceptions
 *
 * @param apiCall The suspend function that makes the HTTP request and returns HttpResponse
 * @param onSuccess The function to parse the response body when status is OK
 * @param errorMessage Optional custom error message prefix (defaults to "API call failed")
 * @return Result<T> containing either the parsed response or a failure with appropriate exception
 */
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
    crossinline onSuccess: suspend (HttpResponse) -> T,
    errorMessage: String = "API call failed"
): Result<T> {
    return try {
        val response = apiCall()
        
        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            try {
                Result.success(onSuccess(response))
            } catch (e: kotlinx.serialization.SerializationException) {
                Result.failure(Exception("Failed to parse response: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to process response: ${e.message}"))
            }
        } else {
            // Handle HTTP error status codes
            val error = when (response.status) {
                HttpStatusCode.Unauthorized -> ApiException.Unauthorized()
                HttpStatusCode.NotFound -> ApiException.NotFound()
                else -> ApiException.ServerError(response.status.value)
            }
            Result.failure(error)
        }
    } catch (e: NetworkException.NoNetwork) {
        Result.failure(e)
    } catch (e: NetworkException.Timeout) {
        Result.failure(e)
    } catch (e: ApiException) {
        // Re-throw ApiException as-is (Unauthorized, NotFound, ServerError)
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(Exception("$errorMessage: ${e.message}"))
    }
}

/**
 * Simplified version that automatically parses the response body.
 * Use this when you just need to deserialize the response to a type.
 *
 * @param apiCall The suspend function that makes the HTTP request and returns HttpResponse
 * @param errorMessage Optional custom error message prefix
 * @return Result<T> containing either the parsed response body or a failure
 */
suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
    errorMessage: String = "API call failed"
): Result<T> {
    return safeApiCall(
        apiCall = apiCall,
        onSuccess = { response -> response.body<T>() },
        errorMessage = errorMessage
    )
}

/**
 * Version for API calls that return Unit (no response body).
 * Use this for DELETE, PUT operations that just need to check success status.
 *
 * @param apiCall The suspend function that makes the HTTP request and returns HttpResponse
 * @param errorMessage Optional custom error message prefix
 * @return Result<Unit> containing either Unit on success or a failure
 */
suspend inline fun safeApiCallUnit(
    crossinline apiCall: suspend () -> HttpResponse,
    errorMessage: String = "API call failed"
): Result<Unit> {
    return try {
        val response = apiCall()
        
        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            Result.success(Unit)
        } else {
            // Handle HTTP error status codes
            val error = when (response.status) {
                HttpStatusCode.Unauthorized -> ApiException.Unauthorized()
                HttpStatusCode.NotFound -> ApiException.NotFound()
                else -> ApiException.ServerError(response.status.value)
            }
            Result.failure(error)
        }
    } catch (e: NetworkException.NoNetwork) {
        Result.failure(e)
    } catch (e: NetworkException.Timeout) {
        Result.failure(e)
    } catch (e: ApiException) {
        // Re-throw ApiException as-is (Unauthorized, NotFound, ServerError)
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(Exception("$errorMessage: ${e.message}"))
    }
}
