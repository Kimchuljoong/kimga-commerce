package kr.co.kimga.member.intg.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.kimga.member.domain.entity.Account
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.infrastructure.repository.AccountJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
        Account(memberId = 2L, accountType = AccountType.POINT),
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
}