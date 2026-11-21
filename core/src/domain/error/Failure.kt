package com.ranjan.somiq.core.domain.error

/** Domain-level infrastructure / API failure (framework-agnostic). */
sealed interface Failure {
    data object NoInternet : Failure
    data object RequestTimedOut : Failure
    data object Unauthorized : Failure
    data object NotFound : Failure
    data object ServerError : Failure
    data object Unknown : Failure
}
