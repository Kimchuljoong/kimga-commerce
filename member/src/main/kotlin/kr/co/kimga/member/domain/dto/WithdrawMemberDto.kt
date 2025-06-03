package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Member
import java.time.Instant

data class WithdrawMemberDto (
    val withdrawDate: Instant?,
) {
    companion object {
        fun from(member: Member): WithdrawMemberDto {
            return WithdrawMemberDto(member.withdrawAt)
        }
    }
}