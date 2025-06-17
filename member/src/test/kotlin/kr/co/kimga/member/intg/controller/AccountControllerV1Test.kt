package kr.co.kimga.member.intg.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.kimga.member.domain.dto.CreateAccountDto
import kr.co.kimga.member.domain.dto.DecreaseAccountDto
import kr.co.kimga.member.domain.dto.IncreaseAccountDto
import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.infrastructure.repository.AccountJpaRepository
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerV1Test {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var accountRepository: AccountJpaRepository

    private val accounts = listOf(
        Account(memberId = 1L, accountType = AccountType.DEPOSIT),
        Account(memberId = 2L, accountType = AccountType.POINT, balance = 10000.0),
    )

    @BeforeEach
    fun setup() {
        accountRepository.deleteAll()
        accountRepository.flush()
        accountRepository.saveAll(accounts)
        accountRepository.flush()
    }

    @Test
    @DisplayName("회원의 구좌들을 조회할 수 있다")
    fun `can retrieve member accounts`() {

        // given
        val memberId = 1L

        // when
        // then
        mockMvc.get("/api/v1/account/my/accounts") {
            header("X-User-Id", memberId)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].accountType") { value(AccountType.DEPOSIT.name) }
        }
    }

    @Test
    @DisplayName("회원은 구좌를 조회할 수 있다")
    fun `can retrieve member account`() {

        // given
        val memberId = 1L
        val type = "DEPOSIT"

        // when
        // then
        mockMvc.get("/api/v1/account/my/account/$type") {
            header("X-User-Id", memberId)
        }.andExpect {
            status { isOk() }
            jsonPath("$.accountType") { value(AccountType.DEPOSIT.name) }
        }
    }

    @Test
    @DisplayName("회원은 구좌를 조회할 수 있다")
    fun `can create new account when it doesn't exist`() {

        // given
        val memberId = 1L
        val type = AccountType.POINT

        val createAccountDto = CreateAccountDto(memberId, type)

        // when
        // then
        mockMvc.post("/api/v1/account") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createAccountDto)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @DisplayName("구좌에 금액을 추가할 수 있다")
    fun `can increase amount of account`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val amount = 1000.0
        val increaseAccountDto = IncreaseAccountDto(memberId, type, amount)

        // when
        // then
        mockMvc.post("/api/v1/account/increase") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(increaseAccountDto)
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/account/my/account/$type") {
            header("X-User-Id", memberId)
        }.andExpect {
            status { isOk() }
            jsonPath("$.accountType") { value(type.name) }
            jsonPath("$.balance") { value(amount) }
        }
    }

    @Test
    @DisplayName("구좌에 금액을 감소할 수 있다")
    fun `can decrease amount of account`() {

        // given
        val memberId = 2L
        val type = AccountType.POINT
        val amount = 1000.0
        val decreaseAccountDto = DecreaseAccountDto(memberId, type, amount)

        // when
        // then
        mockMvc.post("/api/v1/account/decrease") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(decreaseAccountDto)
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/account/my/account/$type") {
            header("X-User-Id", memberId)
        }.andExpect {
            status { isOk() }
            jsonPath("$.accountType") { value(type.name) }
            jsonPath("$.balance") { value(accounts[1].balance - amount) }
        }
    }

    @Test
    @DisplayName("구좌에 금액을 0보다 작게 감소 시킬 수 있다")
    fun `can not decrease account blow zero`() {

        // given
        val memberId = 1L
        val type = AccountType.DEPOSIT
        val amount = 1000.0
        val decreaseAccountDto = DecreaseAccountDto(memberId, type, amount)

        // when
        // then
        mockMvc.post("/api/v1/account/decrease") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(decreaseAccountDto)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}