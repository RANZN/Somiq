package com.ranjan.somiq.core.logging

import platform.Foundation.NSLog

actual object Log {
    actual fun e(tag: String, message: String) {
        NSLog("[WARN] [%s] %s", tag, message)
    }
}