package kr.co.kimga.member.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey


class JwtProvider (
    @Value("\${jwt.access.secret}")
    private val accessSecret: String,
    @Value("\${jwt.access.exp}")
    private val accessExpSeconds: Long,
    @Value("\${jwt.refresh.secret}")
    private val refreshSecret: String,
    @Value("\${jwt.refresh.exp}")
    private val refreshExpSeconds: Long,
) {

    private val accessSecretKey: SecretKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())
    private val refreshSecretKey: SecretKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray())

    fun generateAccessToken(subject: String): String {
        val now = Instant.now()
        val expiration =  now.plusSeconds(accessExpSeconds)

        return Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(accessSecretKey)
            .compact()
    }

    fun generateRefreshToken(subject: String): String {
        val now = Instant.now()
        val expiration =  now.plusSeconds(refreshExpSeconds)

        return Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(refreshSecretKey)
            .compact()
    }

    fun validateAccessToken(token: String) : Boolean {
        return validateToken(token, accessSecretKey)
    }

    fun validateRefreshToken(token: String) : Boolean {
        return validateToken(token, refreshSecretKey)
    }

    private fun validateToken(token: String, key: SecretKey) = try {
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
        true
    } catch (e: JwtException) {
        false
    }

    fun extractSubjectFromAccessToken(token: String) : Claims {
        return parseClaims(accessSecretKey, token)
    }

    fun extractSubjectFromRefreshToken(token: String) : Claims {
        return parseClaims(refreshSecretKey, token)
    }

    private fun parseClaims(key: SecretKey, token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload


}