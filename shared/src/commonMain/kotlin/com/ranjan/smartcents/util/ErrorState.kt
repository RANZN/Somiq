package com.ranjan.smartcents.util


data class ErrorState<T>(
    val type: T,
    val message: String? = null,
)