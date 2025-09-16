package com.smartcents.server.application.auth

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.authRoutes() {
    val authController by inject<AuthController>()
    routing {
        route("/auth") {
            post("/login") {
                authController.login(call)
            }

            post("/signup") {
                authController.signup(call)
            }
        }
    }
}