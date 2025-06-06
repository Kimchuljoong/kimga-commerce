package kr.co.kimga.member.infrastructure.security.jwt

enum class JwtValidationResult {
    VALID,
    INVALID,
    EXPIRED,
}