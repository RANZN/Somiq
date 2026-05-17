package com.ranjan.somiq.core.presentation.error

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.domain.error.Failure
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.model.asString
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_no_internet
import com.ranjan.somiq.core.resources.error_not_found
import com.ranjan.somiq.core.resources.error_request_timed_out
import com.ranjan.somiq.core.resources.error_server_error
import com.ranjan.somiq.core.resources.error_something_went_wrong
import com.ranjan.somiq.core.resources.error_unauthorized

@Composable
fun AppError.asString(): String = when (this) {
    is AppError.Infrastructure -> failure.toUiText().asString()
    is AppError.Custom -> error.toUiText().asString()
}

@Composable
private fun Failure.toUiText(): UiText = when (this) {
    Failure.NoInternet -> UiText.Resource(Res.string.error_no_internet)
    Failure.RequestTimedOut -> UiText.Resource(Res.string.error_request_timed_out)
    Failure.Unauthorized -> UiText.Resource(Res.string.error_unauthorized)
    Failure.NotFound -> UiText.Resource(Res.string.error_not_found)
    Failure.ServerError -> UiText.Resource(Res.string.error_server_error)
    Failure.Unknown -> UiText.Resource(Res.string.error_something_went_wrong)
}
