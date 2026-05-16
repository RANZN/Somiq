package com.ranjan.somiq.core.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

/**
 * Wraps an API call with consistent error handling for common scenarios:
 * - Network errors (NoNetwork, Timeout)
 * - API errors (Unauthorized, NotFound, ServerError)
 * - Serialization errors
 * - Generic exceptions
 *
 * Map failures in the ViewModel with [Throwable.mapAsUiError] and a feature [UiErrorAdapter]
 * into screen [UiError] types, then resolve strings in the UI with each feature’s `displayText`
 * mapper and [com.ranjan.somiq.core.presentation.error.userFacingErrorText] for generic cases.
 *
 * @param apiCall The suspend function that makes the HTTP request and returns HttpResponse
 * @param onSuccess The function to parse the response body when status is OK
 * @return Result<T> containing either the parsed response or a failure with appropriate exception
 */
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
    crossinline onSuccess: suspend (HttpResponse) -> T,
): Result<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        if (response.status.isSuccess()) {
            try {
                Result.success(onSuccess(response))
            } catch (e: SerializationException) {
                Result.failure(e)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        } else {
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
        Result.failure(e)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}

/**
 * Parses the response body to [T] when the HTTP status is successful.
 */
suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
): Result<T> = safeApiCall(
    apiCall = apiCall,
    onSuccess = { response -> response.body<T>() },
)

/**
 * For calls with no response body (e.g. some DELETE/PUT flows).
 */
suspend inline fun safeApiCallUnit(
    crossinline apiCall: suspend () -> HttpResponse,
): Result<Unit> = safeApiCall(
    apiCall = apiCall,
    onSuccess = { },
)
