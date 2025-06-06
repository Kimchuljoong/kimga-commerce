package kr.co.kimga.member.application.auth.dto

data class RefreshRequestDto (
    val accessToken: String,
    val refreshToken: String,
    val uuid: String
)
