package com.ranjan.somiq.core.platform

import org.koin.mp.KoinPlatform.getKoin
import androidx.core.net.toUri

actual fun readUriToBytes(uri: String): ByteArray? {
    return try {
        val context = getKoin().get<android.content.Context>()
        context.contentResolver.openInputStream(uri.toUri())?.use { it.readBytes() }
    } catch (_: Exception) {
        null
    }
}
