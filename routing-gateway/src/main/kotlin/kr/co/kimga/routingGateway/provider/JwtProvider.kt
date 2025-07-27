package kr.co.kimga.routingGateway.provider

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

class JwtProvider(secret: String) {

    private val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}