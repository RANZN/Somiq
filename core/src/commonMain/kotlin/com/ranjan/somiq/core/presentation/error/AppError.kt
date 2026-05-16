package com.ranjan.somiq.core.presentation.error

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.data.network.ApiException
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.model.asString
import com.ranjan.somiq.core.presentation.viewmodel.BaseScreenError
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_no_internet
import com.ranjan.somiq.core.resources.error_not_found
import com.ranjan.somiq.core.resources.error_request_timed_out
import com.ranjan.somiq.core.resources.error_server_error
import com.ranjan.somiq.core.resources.error_something_went_wrong
import com.ranjan.somiq.core.resources.error_unauthorized

sealed interface AppError {
    data object NoInternet : AppError
    data object RequestTimedOut : AppError
    data object Unauthorized : AppError
    data object NotFound : AppError
    data object ServerError : AppError
    data class Custom(val error: BaseScreenError) : AppError
}

fun Throwable.toAppError(defaultError: BaseScreenError): AppError = when (this) {
    is NetworkException.NoNetwork -> AppError.NoInternet
    is NetworkException.Timeout -> AppError.RequestTimedOut
    is ApiException.Unauthorized -> AppError.Unauthorized
    is ApiException.NotFound -> AppError.NotFound
    is ApiException.ServerError -> AppError.ServerError
    is NetworkException.Generic, is NetworkException.Unknown -> AppError.Custom(defaultError)
    else -> AppError.Custom(defaultError)
}

private fun AppError.toUiText(): UiText {
    return when (this) {
        AppError.NoInternet -> UiText.Resource(Res.string.error_no_internet)
        AppError.RequestTimedOut -> UiText.Resource(Res.string.error_request_timed_out)
        AppError.Unauthorized -> UiText.Resource(Res.string.error_unauthorized)
        AppError.NotFound -> UiText.Resource(Res.string.error_not_found)
        AppError.ServerError -> UiText.Resource(Res.string.error_server_error)
        is AppError.Custom -> {
            error.toUiText() ?: UiText.Resource(Res.string.error_something_went_wrong)
        }
    }
}

@Composable
fun AppError.asString(): String = toUiText().asString()
