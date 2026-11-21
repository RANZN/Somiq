package com.ranjan.somiq.core.logging

expect object Log {
    fun e(tag: String, message: String)
}