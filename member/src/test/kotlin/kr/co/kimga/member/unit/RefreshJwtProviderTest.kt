package kr.co.kimga.member.unit

import kr.co.kimga.member.infrastructure.common.Utils
import kr.co.kimga.member.infrastructure.security.jwt.JwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.JwtValidationResult
import kr.co.kimga.member.infrastructure.security.jwt.RefreshJwtProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import java.util.*
import kotlin.test.assertEquals

class RefreshJwtProviderTest {

    private val secret = "GdThfS3sypHkyMD+ykDdzeXVtDQH3vQ9kI/fTS3tlLg="
    private val exp = 1L

    private lateinit var jwtProvider: JwtProvider

    @BeforeEach
    fun setUp() {
        jwtProvider = RefreshJwtProvider(secret, exp)
    }

    @Test
    @DisplayName("유효한 Refresh Token을 생성할 수 있다")
    fun `can generate valid refresh token`() {
        // given
        val uuid = Utils.generateUuid()

        // when
        val token = jwtProvider.generate(uuid)
        val validateResult = jwtProvider.validate(token)

        // then
        assertNotNull(token)
        assertEquals(JwtValidationResult.VALID, validateResult)
    }

    @Test
    @DisplayName("만료된 Refresh Token 검증할 수 있다")
    fun `can validate expired refresh token`() {
        // given
        val uuid = Utils.generateUuid()
        val token = jwtProvider.generate(uuid)

        // when
        Thread.sleep(1000L)
        val validateResult = jwtProvider.validate(token)

        // then
        assertNotNull(token)
        assertEquals(JwtValidationResult.EXPIRED, validateResult)
    }

    @Test
    @DisplayName("변조된 Refresh Token을 검증할 수 있다")
    fun `can validate forged refresh token`() {

        // given
        val uuid = Utils.generateUuid()
        val modifyUuid = Utils.generateUuid()
        val token = jwtProvider.generate(uuid)

        // when
        val forgedToken = forgeToken(token, uuid, modifyUuid)
        val validationResult = jwtProvider.validate(forgedToken)

        // then
        assertEquals(JwtValidationResult.INVALID, validationResult)

    }

    private fun forgeToken(token: String, uuid: String, modifyUuid: String): String {
        val parts = token.split(".")
        val header = parts[0]
        val payload = parts[1]
        val signature = parts[2]

        val modifiedPayload = String(Base64.getUrlDecoder().decode(payload))
            .replace(uuid, modifyUuid)
        val encodedPayload = Base64.getUrlEncoder().encodeToString(modifiedPayload.toByteArray())

        return "$header.$encodedPayload.$signature"
    }
}