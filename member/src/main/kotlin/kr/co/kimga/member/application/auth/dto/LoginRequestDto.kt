package kr.co.kimga.member.application.auth.dto


data class LoginRequestDto (
    val email: String,
    val password: String
)