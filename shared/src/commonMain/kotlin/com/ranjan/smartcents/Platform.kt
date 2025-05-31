package com.ranjan.smartcents

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform