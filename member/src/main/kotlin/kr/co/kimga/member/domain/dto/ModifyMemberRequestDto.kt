package kr.co.kimga.member.domain.dto

import jakarta.validation.constraints.Size

data class ModifyMemberRequestDto(
    val id: Long,
    @field:Size(min = 2, message = "이름은 2글자 이상이어야 합니다")
    val name: String,
)
