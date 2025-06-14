package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType

data class CreateAccountDto(
    val memberId: Long,
    val accountType: AccountType
) {
    fun toEntity(): Account {
        return Account(memberId = memberId, accountType = accountType)
    }
}
