package kr.co.kimga.member.application.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class LoginRequestDto (
    @field:NotBlank(message = "이메일은 비어있을 수 없습니다")
    @field:Email(message = "이메일 형식이 올바르지 않습니다")
    val email: String,
    @field:NotBlank(message = "패스워드는 비어있을 수 없습니다")
    val password: String
)