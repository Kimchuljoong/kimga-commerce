package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType

data class MemberAccountDto(
    val accountId: Long,
    val accountType: AccountType,
    val balance: Double
) {
    companion object {
        fun of(account: Account): MemberAccountDto {
            return MemberAccountDto(account.id!!, account.accountType!!, account.balance)
        }
    }
}
