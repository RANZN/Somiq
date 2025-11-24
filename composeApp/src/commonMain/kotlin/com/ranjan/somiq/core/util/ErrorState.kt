package com.ranjan.somiq.core.util


data class ErrorState<T>(
    val type: T,
    val message: String? = null,
)