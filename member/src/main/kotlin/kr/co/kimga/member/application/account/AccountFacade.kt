package kr.co.kimga.member.application.account

import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.IncreaseAccountDto
import kr.co.kimga.member.domain.dto.DecreaseAccountDto
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.domain.service.AccountService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class AccountFacade (
    private val accountService: AccountService
) {

    fun findMemberAccounts(memberId: Long) = accountService.findMemberAllAccounts(memberId)

    fun findMemberAccount(memberId: Long, accountType: AccountType) = accountService.findMemberAccount(memberId, accountType)

    @Transactional
    fun createAccount(createAccountDto: CreateAccountDto) = accountService.createAccount(createAccountDto)

    @Transactional
    fun increaseAccount(increaseAccountDto: IncreaseAccountDto) = accountService.increaseAccount(increaseAccountDto)

    @Transactional
    fun decreaseAccount(decreaseAccountDto: DecreaseAccountDto) = accountService.decreaseAccount(decreaseAccountDto)

}