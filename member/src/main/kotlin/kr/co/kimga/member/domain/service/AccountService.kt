package kr.co.kimga.member.domain.service

import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.DepositAccountDto
import kr.co.kimga.member.domain.dto.WithdrawAccountDto
import kr.co.kimga.member.domain.entity.Account
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

    @Transactional
    fun createAccount(createAccount: CreateAccountDto) {
        accountRepository.findAccountByMemberIdAndAccountType(createAccount.memberId, createAccount.accountType)
            ?.let { throw AccountAlreadyCreatedException() }
        accountRepository.save(Account(memberId = createAccount.memberId, accountType = createAccount.accountType))
    }

    @Transactional
    fun depositAccount(depositAccountDto: DepositAccountDto) {
        val account =
            accountRepository.findAccountByMemberIdAndAccountType(depositAccountDto.memberId, depositAccountDto.accountType)
                ?: let { throw AccountNotFoundException() }
        account.increaseAmount(depositAccountDto.amount)
    }

    @Transactional
    fun withdrawAccount(withdrawAccountDto: WithdrawAccountDto) {
        val account =
            accountRepository.findAccountByMemberIdAndAccountType(withdrawAccountDto.memberId, withdrawAccountDto.accountType)
                ?: let { throw AccountNotFoundException() }
        account.decreaseAmount(withdrawAccountDto.amount)
    }
}