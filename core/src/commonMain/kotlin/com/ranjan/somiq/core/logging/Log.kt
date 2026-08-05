package com.ranjan.somiq.core.logging

internal expect object Log {
    fun e(tag: String, message: String, throwable: Throwable? = null)
}