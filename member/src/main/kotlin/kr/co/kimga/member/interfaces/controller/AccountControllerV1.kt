package kr.co.kimga.member.interfaces.controller

import kr.co.kimga.member.application.account.AccountFacade
import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.IncreaseAccountDto
import kr.co.kimga.member.domain.dto.DecreaseAccountDto
import kr.co.kimga.member.domain.entity.enums.AccountType
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
class AccountControllerV1(
    private val accountFacade: AccountFacade
) {

    @GetMapping("/my/accounts")
    fun findAccounts(
        @RequestHeader("X-User-Id") memberId: Long
    ) = accountFacade.findMemberAccounts(memberId)

    @GetMapping("/my/account/{accountType}")
    fun findAccount(
        @RequestHeader("X-User-Id") memberId: Long,
        @PathVariable("accountType") accountType: AccountType
    ) = accountFacade.findMemberAccount(memberId, accountType)

    @PostMapping("")
    fun createAccount(
        @RequestBody createAccountDto: CreateAccountDto
    ) = accountFacade.createAccount(createAccountDto)

    @PostMapping("/increase")
    fun increase(
        @RequestBody increaseAccountDto: IncreaseAccountDto
    ) = accountFacade.increaseAccount(increaseAccountDto)

    @PostMapping("/decrease")
    fun decrease(
        @RequestBody decreaseAccountDto: DecreaseAccountDto
    ) = accountFacade.decreaseAccount(decreaseAccountDto)

}