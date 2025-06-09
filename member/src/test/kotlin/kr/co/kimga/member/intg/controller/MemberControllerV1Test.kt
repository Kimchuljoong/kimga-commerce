package kr.co.kimga.member.intg.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.TokenDto
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import kr.co.kimga.member.interfaces.controller.dto.MemberCreateRequestDto
import kr.co.kimga.member.interfaces.controller.dto.MemberModifyRequestDto
import kr.co.kimga.member.intg.config.RedisTestConfig
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerV1Test: RedisTestConfig() {

    private val email = "test@test.com"
    private val password = "test1234!@#$"
    private val name = "test"

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var memberRepository: MemberJpaRepository

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        if (testInfo.tags.contains("skip"))
            return

        val member = Member(
            email = email,
            password = password,
            name = name
        )
        memberRepository.save(member)
    }

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
        memberRepository.deleteAll()
    }

    @Tag("skip")
    @Test
    @DisplayName("회원을 신규로 생성할 수 있다")
    fun `can create a member`() {

        // given
        val memberCreateRequestDto = MemberCreateRequestDto(
            email = email,
            password = password,
            name = name
        )

        // when
        // then
        mockMvc.post("/api/v1/member/new") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(memberCreateRequestDto)
        }.andExpect {
            status { isCreated() }
        }

        val createdMember = memberRepository.findByEmailAndPasswordAndWithdrawYn(email, password)
        assertNotNull(createdMember)
        assertEquals(name, createdMember.name)
    }

    @Test
    @DisplayName("이미 생성된 이메일로 회원을 생성할 수 없다")
    fun `can not create member with already existing email`() {

        // given
        val memberCreateRequestDto = MemberCreateRequestDto(
            email = email,
            password = password,
            name = name
        )

        // when
        // then
        mockMvc.post("/api/v1/member/new") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(memberCreateRequestDto)
        }.andExpect {
            status { isConflict() }
        }
    }

    @Test
    @DisplayName("회원 정보를 변경할 수 있다")
    fun `can modify member info`() {
        // given
        val changeName = "test2"
        val memberModifyRequestDto = MemberModifyRequestDto(
            name = changeName
        )
        val tokenDto = login()

        // when
        mockMvc.put("/api/v1/member") {
            header("Authorization", "Bearer ${tokenDto.accessToken}")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(memberModifyRequestDto)
        }.andExpect {
            status { isOk() }
        }

        // then
        val member = memberRepository.findByEmailAndPasswordAndWithdrawYn(email, password)
        assertNotNull(member)
        assertEquals(changeName, member.name)
    }

    @Test
    @DisplayName("회원 탈퇴를 할 수 있다")
    fun `member can withdraw`() {
        // given
        val tokenDto = login()

        // when
        // then
        mockMvc.delete("/api/v1/member") {
            header("Authorization", "Bearer ${tokenDto.accessToken}")
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        val member = memberRepository.findByEmailAndPasswordAndWithdrawYn(email, password)
        assertNull(member)
    }

    private fun login() : TokenDto {
        val loginRequestDto = LoginRequestDto(
            email = email,
            password = password
        )

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.accessToken") { exists() }
            jsonPath("$.refreshToken") { exists() }
        }

        val loginResult = mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val loginResponseBody = loginResult.response.contentAsString
        val loginResponse = objectMapper.readValue(loginResponseBody, TokenDto::class.java)

        return loginResponse
    }
}