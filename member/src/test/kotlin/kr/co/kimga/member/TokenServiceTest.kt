package kr.co.kimga.member

import kr.co.kimga.member.domain.exception.CanNotRenewTokenException
import kr.co.kimga.member.domain.service.TokenService
import kr.co.kimga.member.infrastructure.common.Utils
import kr.co.kimga.member.infrastructure.security.jwt.AccessJwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.JwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.RefreshJwtProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TokenServiceTest {

    private lateinit var accessJwtProvider: JwtProvider
    private lateinit var refreshJwtProvider: JwtProvider
    private lateinit var tokenService: TokenService

    private val secret = "GdThfS3sypHkyMD+ykDdzeXVtDQH3vQ9kI/fTS3tlLg="
    private val exp = 1L

    @BeforeEach
    fun serUp() {
        accessJwtProvider = AccessJwtProvider(secret, exp)
        refreshJwtProvider = RefreshJwtProvider(secret, exp+2L)
        tokenService = TokenService(accessJwtProvider, refreshJwtProvider)
    }

    @Test
    @DisplayName("토큰 서비스를 통해 신규 토큰을 발행할 수 있다")
    fun `can generate new tokens with token service`() {
        // given
        val uuid = Utils.generateUuid()

        // when
        val (accessToken, refreshToken) = tokenService.makeNewToken(uuid)
        val accessSubject = accessJwtProvider.extractClaims(accessToken).subject
        val refreshSubject = refreshJwtProvider.extractClaims(refreshToken).subject

        // then
        assertEquals(uuid, accessSubject)
        assertEquals(uuid, refreshSubject)
    }

    @Test
    @DisplayName("만료되지 않은 RefreshToken을 통해 토큰을 리프레시 할 수 있다")
    fun `can refresh token with expired access token and alive refresh token`() {
        // given
        val uuid = Utils.generateUuid()
        val (accessToken, refreshToken) = tokenService.makeNewToken(uuid)

        // when
        Thread.sleep(2000L)
        val newUuid = Utils.generateUuid()
        val (renewedAccessToken, renewedRefreshToken) = tokenService.renewToken(refreshToken, newUuid)
        val renewedAccessSubject = accessJwtProvider.extractClaims(renewedAccessToken).subject
        val renewedRefreshSubject = refreshJwtProvider.extractClaims(renewedRefreshToken).subject

        // then
        assertNotEquals(uuid, renewedAccessSubject)
        assertNotEquals(uuid, renewedRefreshSubject)
        assertEquals(newUuid, renewedAccessSubject)
        assertEquals(newUuid, renewedRefreshSubject)
    }

    @Test
    @DisplayName("만료된 RefreshToken을 통해 토큰을 리프레시 할 수 없다")
    fun `can not refresh token with expired access token and expired refresh token`() {
        // given
        val uuid = Utils.generateUuid()
        val (accessToken, refreshToken) = tokenService.makeNewToken(uuid)

        // when
        // then
        Thread.sleep(3000L)
        val newUuid = Utils.generateUuid()
        assertThrows<CanNotRenewTokenException> {
            tokenService.renewToken(refreshToken, newUuid)
        }
    }
}