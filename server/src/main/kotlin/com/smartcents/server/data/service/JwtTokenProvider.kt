package com.smartcents.server.data.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.smartcents.server.domain.model.User
import com.smartcents.server.domain.service.TokenProvider
import java.util.Date
import kotlin.time.Duration.Companion.hours

class JwtTokenProvider(
    private val secret: String,
    private val issuer: String,
    private val audience: String
) : TokenProvider {

    override fun createToken(user: User): String {
        val validity = Date(System.currentTimeMillis() + 24.hours.inWholeMilliseconds)

        return JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("email", user.email)
            .withClaim("name", user.name)
            .withExpiresAt(validity)
            .sign(Algorithm.HMAC256(secret))
    }

}