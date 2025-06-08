package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Member
import java.time.Instant

data class WithdrawedMemberDto (
    val withdrawDate: Instant?,
) {
    companion object {
        fun of(member: Member): WithdrawedMemberDto {
            return WithdrawedMemberDto(member.withdrawAt)
        }
    }
}