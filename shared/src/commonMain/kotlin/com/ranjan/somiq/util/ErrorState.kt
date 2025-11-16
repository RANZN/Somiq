package com.ranjan.somiq.util


data class ErrorState<T>(
    val type: T,
    val message: String? = null,
)