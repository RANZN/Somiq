package com.smartcents.server.application

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.checkHealth() {
    routing {
        get("/") {
            call.respondText("Server is running!", ContentType.Text.Plain)
        }
    }
}