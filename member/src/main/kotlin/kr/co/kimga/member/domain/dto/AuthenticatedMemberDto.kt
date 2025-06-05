package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Member

data class AuthenticatedMemberDto (
    val id: Long,
    val name: String,
) {
    companion object {
        fun of(member: Member): AuthenticatedMemberDto {
            return AuthenticatedMemberDto(member.id!!, member.name)
        }
    }
}
