package kr.co.kimga.member.intg.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.redis.testcontainers.RedisContainer
import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.TokenDto
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import kr.co.kimga.member.intg.config.RedisTestConfig
import kr.co.kimga.member.interfaces.controller.dto.AuthRefreshRequestDto
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
import kotlin.test.assertNotEquals

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerV1Test: RedisTestConfig() { // 현재 개별 테스트 수행만 가능

    @Autowired
    lateinit var memberRepository: MemberJpaRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val email = "test@test.com"
    private val password = "test1123!@#$"
    private val name = "test"

    @BeforeEach
    fun setUp() {
        val member = Member(
            email = email,
            password = password,
            name = name,
        )
        memberRepository.save(member)
    }

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("로그인을 할 수 있다")
    fun `member can login`() {

        // given
        val loginRequestDto = LoginRequestDto(
            email = email,
            password = password
        )

        // when
        // then
        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.accessToken") { exists() }
            jsonPath("$.refreshToken") { exists() }
        }
    }

    @Test
    @DisplayName("없는 회원 정보로 로그인을 할 수 없다")
    fun `invalid member can not login`() {

        // given
        val loginRequestDto = LoginRequestDto(
            email = "test1@test.com",
            password = password
        )

        // when
        // then
        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("로그인한 회원은 토큰은 리프레시 할 수 있다")
    fun `member who logged in can refresh token`() {

        // given
        val loginRequestDto = LoginRequestDto(
            email = email,
            password = password
        )

        val loginResult = mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val loginResponseBody = loginResult.response.contentAsString
        val loginResponse = objectMapper.readValue(loginResponseBody, TokenDto::class.java)
        val accessToken = loginResponse.accessToken
        val refreshToken = loginResponse.refreshToken

        val authRefreshRequestDto = AuthRefreshRequestDto(
            refreshToken = refreshToken,
        )
        // when
        // then
        val refreshResult = mockMvc.put("/api/v1/auth/refresh") {
            header("Authorization", "Bearer $accessToken")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(authRefreshRequestDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.accessToken") { exists() }
            jsonPath("$.refreshToken") { exists() }
        }.andReturn()

        val refreshResponseBody = refreshResult.response.contentAsString
        val refreshResponse = objectMapper.readValue(refreshResponseBody, TokenDto::class.java)
        val refreshedAccessToken = refreshResponse.refreshToken
        val refreshedRefreshToken = refreshResponse.refreshToken

        assertNotEquals(accessToken, refreshedAccessToken)
        assertNotEquals(refreshToken, refreshedRefreshToken)
    }

    @Test
    @DisplayName("로그인한 회원은 로그아웃 할 수 있다")
    fun `member who logged in can logout`() {

        // given
        val loginRequestDto = LoginRequestDto(
            email = email,
            password = password
        )
        val result = mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequestDto)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val responseBody = result.response.contentAsString
        val loginResponse = objectMapper.readValue(responseBody, TokenDto::class.java)
        val accessToken = loginResponse.accessToken

        // when
        // then
        mockMvc.delete("/api/v1/auth/logout") {
            header("Authorization", "Bearer $accessToken")
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

}