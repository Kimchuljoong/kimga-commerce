package kr.co.kimga.member.infrastructure.repository

import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountJpaRepository: JpaRepository<Account, Long> {

    fun findAccountByMemberIdAndAccountType(memberId: Long, accountType: AccountType): Account?

    fun findAccountsByMemberId(memberId: Long): List<Account>
}