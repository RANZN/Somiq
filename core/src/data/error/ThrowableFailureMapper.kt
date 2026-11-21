package com.ranjan.somiq.core.data.error

import com.ranjan.somiq.core.data.network.ApiException
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.domain.error.Failure

fun Throwable.toFailure(): Failure = when (this) {
    is NetworkException.NoNetwork -> Failure.NoInternet
    is NetworkException.Timeout -> Failure.RequestTimedOut
    is ApiException.Unauthorized -> Failure.Unauthorized
    is ApiException.NotFound -> Failure.NotFound
    is ApiException.ServerError -> Failure.ServerError
    else -> Failure.Unknown
}
