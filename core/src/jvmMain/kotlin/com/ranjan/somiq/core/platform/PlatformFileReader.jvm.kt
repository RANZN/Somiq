package com.ranjan.somiq.core.platform

import java.io.File
import java.net.URI

actual fun readUriToBytes(uri: String): ByteArray? {
    return try {
        when {
            uri.startsWith("file:") -> File(URI.create(uri)).readBytes()
            else -> null
        }
    } catch (_: Exception) {
        null
    }
}
