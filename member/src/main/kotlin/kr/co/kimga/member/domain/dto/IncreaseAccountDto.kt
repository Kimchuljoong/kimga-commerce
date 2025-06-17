package kr.co.kimga.member.domain.dto

import kr.co.kimga.member.domain.entity.enums.AccountType

data class DepositAccountDto(
    val memberId: Long,
    val accountType: AccountType,
    val amount: Double
)
