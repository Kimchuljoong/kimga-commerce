package kr.co.kimga.member.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kr.co.kimga.member.domain.entity.Member

data class CreateMemberRequestDto(
    @field:NotBlank(message = "이메일은 필수 입니다")
    @field:Email(message = "이메일 형식이 올바르지 않습니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입니다")
    @field:Size(min = 10, message = "비밀번호는 10자 이상 이어야 합니다")
    @field:Size(max = 15, message = "비밀번호는 15자를 초과할 수 없습니다")
    @field:Pattern(
        regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]*$",
        message = "비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다"
    )
    val password: String,

    @field:NotBlank(message = "이름은 비워둘 수 없습니다")
    @field:Size(min = 2, message = "이름은 최소 2글자 이상 작성해야 합니다")
    val name: String

) {
    fun toEntity(): Member {
        return Member(
            email = email,
            password = password,
            name = name
        )
    }
}