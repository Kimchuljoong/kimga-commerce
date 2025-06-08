package kr.co.kimga.member.unit

import jakarta.validation.Validation
import jakarta.validation.Validator
import kr.co.kimga.member.domain.dto.ModifyMemberDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModifyMemberDtoTest {

    private lateinit var validator : Validator

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    @DisplayName("수정하는 이름은 빈 값일 수 없다")
    fun `modify name can not empty`() {

        // given
        val modifyMemberDto = ModifyMemberDto(id = 1, name = "")

        // when
        val violations = validator.validate(modifyMemberDto)

        // then
        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("name", violation.propertyPath.toString())
        assertEquals("이름은 2글자 이상이어야 합니다", violation.message)
    }
}