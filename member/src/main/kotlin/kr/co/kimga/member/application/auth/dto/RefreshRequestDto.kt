package kr.co.kimga.member.application.auth.dto

import jakarta.validation.constraints.NotBlank

data class RefreshRequestDto (
    @field:NotBlank
    val refreshToken: String,
    @field:NotBlank
    val uuid: String
)
