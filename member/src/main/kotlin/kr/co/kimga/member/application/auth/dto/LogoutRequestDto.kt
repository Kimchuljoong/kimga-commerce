package kr.co.kimga.member.application.auth.dto

import jakarta.validation.constraints.NotBlank

data class LogoutRequestDto (
    @field:NotBlank
    val uuid: String
)
