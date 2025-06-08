package kr.co.kimga.member.unit

import kr.co.kimga.member.infrastructure.common.Utils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertNotEquals

class UtilsTest {

    @Test
    @DisplayName("UUID를 생성할 수 있다")
    fun `can generate UUID`() {
        // given
        val generatedUuid = Utils.generateUuid()

        // when
        // then
        println (generatedUuid)
        assertNotNull(generatedUuid)
        assertNotEquals(generatedUuid, "")
    }
}