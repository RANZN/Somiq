package com.ranjan.somiq

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform