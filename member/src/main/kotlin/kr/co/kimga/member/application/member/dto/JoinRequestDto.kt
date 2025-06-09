package kr.co.kimga.member.application.member.dto

data class JoinRequestDto(
    val email: String,
    val password: String,
    val name: String,
)
