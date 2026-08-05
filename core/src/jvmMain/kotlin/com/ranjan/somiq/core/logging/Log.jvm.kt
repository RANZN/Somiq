package com.ranjan.somiq.core.logging

internal actual object Log {
    actual fun e(tag: String, message: String, throwable: Throwable?) = Unit
}