package kr.co.kimga.member.interfaces.controller.dto

import jakarta.validation.constraints.NotBlank

data class AuthRefreshRequestDto(
    @field:NotBlank
    val refreshToken: String
)