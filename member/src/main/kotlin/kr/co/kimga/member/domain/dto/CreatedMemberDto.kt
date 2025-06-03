package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Member

data class CreatedMemberDto (
    val id: Long,
    val email: String,
    val name: String,
) {
    companion object {
        fun from(createdMember: Member) : CreatedMemberDto {
            return CreatedMemberDto(createdMember.id!!, createdMember.email, createdMember.name)
        }
    }
}