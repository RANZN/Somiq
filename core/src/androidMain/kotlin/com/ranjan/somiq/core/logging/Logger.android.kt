package com.ranjan.somiq.core.logging

actual object Log {
    actual fun e(tag: String, message: String) {
        android.util.Log.e(tag, message)
    }
}