package kr.co.kimga.member.application.auth.dto


data class AuthenticateRequestDto (
    val email: String,
    val password: String
)