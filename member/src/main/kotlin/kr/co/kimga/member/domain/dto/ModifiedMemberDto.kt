package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Member

data class ModifiedMemberDto (
    val id: Long,
    val name: String
) {
    companion object {
        fun from(member: Member) : ModifiedMemberDto {
            return ModifiedMemberDto(member.id!!, member.name)
        }
    }
}