package com.ranjan.somiq.core.presentation.error

import com.ranjan.somiq.core.data.error.toFailure
import com.ranjan.somiq.core.domain.error.Failure

fun Throwable.toAppError(fallback: BaseScreenError): AppError =
    toFailure().toAppError(fallback)

fun Failure.toAppError(fallback: BaseScreenError): AppError = when (this) {
    Failure.Unknown -> AppError.Custom(fallback)
    else -> AppError.Infrastructure(this)
}