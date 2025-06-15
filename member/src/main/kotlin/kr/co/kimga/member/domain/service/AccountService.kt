package kr.co.kimga.member.domain.service

import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.DepositAccountDto
import kr.co.kimga.member.domain.dto.WithdrawAccountDto
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

    fun findAllAccounts(): List<Account> {
        return accountRepository.findAll()
    }

    @Transactional
    fun createAccount(createAccount: CreateAccountDto) {
        checkAccountCreated(createAccount.memberId, createAccount.accountType)
        accountRepository.save(Account(memberId = createAccount.memberId, accountType = createAccount.accountType))
    }

    @Transactional
    fun depositAccount(depositAccountDto: DepositAccountDto) {
        val account = findAccount(depositAccountDto.memberId, depositAccountDto.accountType)
        account.increaseAmount(depositAccountDto.amount)
    }

    @Transactional
    fun withdrawAccount(withdrawAccountDto: WithdrawAccountDto) {
        val account = findAccount(withdrawAccountDto.memberId, withdrawAccountDto.accountType)
        account.decreaseAmount(withdrawAccountDto.amount)
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