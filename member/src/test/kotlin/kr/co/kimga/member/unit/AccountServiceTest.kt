package kr.co.kimga.member.unit

import io.mockk.every
import io.mockk.mockk
import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.domain.service.AccountService
import kr.co.kimga.member.infrastructure.repository.AccountJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*

class AccountServiceTest {

    private lateinit var accountRepository: AccountJpaRepository
    private lateinit var accountService: AccountService

    @BeforeEach
    fun setUp() {
        accountRepository = mockk()
        accountService = AccountService(accountRepository)
    }

    @Test
    @DisplayName("생성된 구좌가 없으면 구좌를 생성할 수 있다")
    fun `can create account when it was not created`() {
        // given
        val createAccountDto = CreateAccountDto(1L, AccountType.DEPOSIT)

        val fakeAccount = Account(1L, 1L, AccountType.DEPOSIT)

        every {
            accountRepository.findAccountByMemberIdAndAccountType(any(), any())
        } returns null

        every {
            accountRepository.save(any())
        } returns fakeAccount

        // when
        // then
        assertDoesNotThrow { accountService.createAccount(createAccountDto) }
    }



}