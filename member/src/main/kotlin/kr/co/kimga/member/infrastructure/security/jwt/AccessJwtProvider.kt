package kr.co.kimga.member.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import javax.crypto.SecretKey

@Component
class AccessJwtProvider (
    @Value("\${jwt.access.secret}")
    private val secret: String,
    @Value("\${jwt.access.exp}")
    private val expSeconds: Long,
) : JwtProvider {

    override val secretKey: SecretKey
        get() = Keys.hmacShaKeyFor (secret.toByteArray())

    override fun generate(subject: String): String =
        generateToken(subject, Instant.now(), Instant.now().plusSeconds(expSeconds))

    override fun validate(token: String) : JwtValidationResult = validateToken(token)

    override fun extractSubject(token: String) : Claims = parseClaims(token)

}