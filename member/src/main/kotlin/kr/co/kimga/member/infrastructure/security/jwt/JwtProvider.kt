package kr.co.kimga.member.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

interface JwtProvider {

    val secretKey: SecretKey

    fun generate(subject: String): String
    fun validate(token: String) : JwtValidationResult
    fun extractClaims(token: String) : Claims

    fun generateToken(subject: String, now: Instant, expiration: Instant): String =
        Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(secretKey)
            .compact()

    fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun validateToken(token: String) = try {
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)

        JwtValidationResult.VALID
    } catch (e: ExpiredJwtException) {
        JwtValidationResult.EXPIRED
    } catch (e: JwtException) {
        JwtValidationResult.INVALID
    }
}