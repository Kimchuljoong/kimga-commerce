package kr.co.kimga.member.domain.service

import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.IncreaseAccountDto
import kr.co.kimga.member.domain.dto.MemberAccountDto
import kr.co.kimga.member.domain.dto.DecreaseAccountDto
import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.domain.exception.AccountAlreadyCreatedException
import kr.co.kimga.member.domain.exception.AccountNotFoundException
import kr.co.kimga.member.infrastructure.repository.AccountJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class AccountService(
    private val accountRepository: AccountJpaRepository
) {

    fun findMemberAllAccounts(memberId: Long): List<MemberAccountDto> {
        return accountRepository.findAccountsByMemberId(memberId).map {
            MemberAccountDto(it.id!!, it.accountType!!, it.balance)
        }
    }

    fun findMemberAccount(memberId: Long, accountType: AccountType): MemberAccountDto {
        val findAccount = findAccount(memberId, accountType)
        return MemberAccountDto.of(findAccount)

    }

    @Transactional
    fun createAccount(createAccount: CreateAccountDto) {
        checkAccountCreated(createAccount.memberId, createAccount.accountType)
        accountRepository.save(Account(memberId = createAccount.memberId, accountType = createAccount.accountType))
    }

    @Transactional
    fun increaseAccount(increaseAccountDto: IncreaseAccountDto) {
        val account = findAccount(increaseAccountDto.memberId, increaseAccountDto.accountType)
        account.increaseAmount(increaseAccountDto.amount)
    }

    @Transactional
    fun decreaseAccount(decreaseAccountDto: DecreaseAccountDto) {
        val account = findAccount(decreaseAccountDto.memberId, decreaseAccountDto.accountType)
        account.decreaseAmount(decreaseAccountDto.amount)
    }

    private fun checkAccountCreated(
        memberId: Long,
        accountType: AccountType) {
        val account =
            accountRepository.findAccountByMemberIdAndAccountType(memberId, accountType)
        if (account != null) {
            throw AccountAlreadyCreatedException()
        }
    }

    private fun findAccount(
        memberId: Long,
        accountType: AccountType
    ) = (accountRepository.findAccountByMemberIdAndAccountType(memberId, accountType)
        ?: let { throw AccountNotFoundException() })
}