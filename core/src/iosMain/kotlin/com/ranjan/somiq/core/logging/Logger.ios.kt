package com.ranjan.somiq.core.logging

import platform.Foundation.NSLog

internal actual object Log {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        NSLog("[WARN] [%s] %s", tag, message, throwable)
    }
}