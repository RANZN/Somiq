package com.ranjan.somiq.core.presentation.error

import com.ranjan.somiq.core.domain.error.Failure

sealed interface AppError {
    data class Infrastructure(val failure: Failure) : AppError
    data class Custom(val error: BaseScreenError) : AppError
}