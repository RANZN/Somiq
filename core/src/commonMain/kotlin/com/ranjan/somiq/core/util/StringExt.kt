package com.ranjan.somiq.core.util

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(this)
}

/** Digits only, minimum length 10 (E.164-style local part). */
fun String.isValidPhone(): Boolean {
    val digits = filter { it.isDigit() }
    return digits.length in 10..15
}

fun Long.toMinSecFormat(): String {
    val minutes = this / 60
    val seconds = this % 60
    val minStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secStr = if (seconds < 10) "0$seconds" else "$seconds"
    return "$minStr:$secStr"
}