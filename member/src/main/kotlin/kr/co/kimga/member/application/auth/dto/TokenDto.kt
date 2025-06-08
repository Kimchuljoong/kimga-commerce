package kr.co.kimga.member.application.auth.dto

data class TokenDto (
    val accessToken: String,
    val refreshToken: String
)