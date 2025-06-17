package kr.co.kimga.member.unit

import io.mockk.every
import io.mockk.mockk
import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.IncreaseAccountDto
import kr.co.kimga.member.domain.dto.DecreaseAccountDto
import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.domain.exception.AccountAlreadyCreatedException
import kr.co.kimga.member.domain.service.AccountService
import kr.co.kimga.member.infrastructure.repository.AccountJpaRepository
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

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

    @Test
    @DisplayName("이미 생성된 구좌는 생성할 수 없다")
    fun `can create account when it was created`() {

        // given
        val createAccountDto = CreateAccountDto(1L, AccountType.DEPOSIT)

        val fakeAccount = Account(1L, 1L, AccountType.DEPOSIT)

        every {
            accountRepository.findAccountByMemberIdAndAccountType(any(), any())
        } returns fakeAccount

        // when
        // then
        assertThrows<AccountAlreadyCreatedException> { accountService.createAccount(createAccountDto) }
    }

    @Test
    @DisplayName("구좌에 금액을 추가할 수 있다")
    fun `can increase amount to account`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val amount = 10.0
        val fakeAccount = Account(1L, memberId, type)
        val increaseAccountDto = IncreaseAccountDto(memberId, AccountType.DEPOSIT, amount)

        every {
            accountRepository.findAccountByMemberIdAndAccountType(any(), any())
        } returns fakeAccount

        // when
        accountService.increaseAccount(increaseAccountDto)

        // then
        assertEquals(amount, fakeAccount.balance)
    }

    @Test
    @DisplayName("구좌에 금액을 뺄 수 있다")
    fun `can decrease amount to account`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val amount = 10.0
        val balance = 100.0
        val fakeAccount = Account(1L, memberId, type, balance)
        val decreaseAccountDto = DecreaseAccountDto(memberId, AccountType.DEPOSIT, amount)

        every {
            accountRepository.findAccountByMemberIdAndAccountType(any(), any())
        } returns fakeAccount

        // when
        accountService.decreaseAccount(decreaseAccountDto)

        // then
        assertEquals(balance - amount, fakeAccount.balance)
    }

    @Test
    @DisplayName("회원의 모든 구좌를 조회할 수 있다")
    fun `can retrieve all accounts for member`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val balance = 100.0
        val fakeAccount = Account(1L, memberId, type, balance)

        every {
            accountRepository.findAccountsByMemberId(any())
        } returns listOf(fakeAccount)

        // when
        val findMemberAllAccounts = accountService.findMemberAllAccounts(memberId)

        // then
        assertEquals(1, findMemberAllAccounts.size)
        assertEquals(memberId, findMemberAllAccounts.first().accountId)
    }

    @Test
    @DisplayName("구좌를 조회할 수 있다")
    fun `can retrieve account for member`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val balance = 100.0
        val fakeAccount = Account(1L, memberId, type, balance)

        every {
            accountRepository.findAccountByMemberIdAndAccountType(any(), any())
        } returns fakeAccount

        // when
        val findMemberAccount = accountService.findMemberAccount(memberId, type)

        // then
        assertEquals(memberId, findMemberAccount.accountId)
        assertEquals(type, findMemberAccount.accountType)
    }


}