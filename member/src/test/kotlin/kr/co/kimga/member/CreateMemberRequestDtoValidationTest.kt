package kr.co.kimga.member

import jakarta.validation.Validation
import jakarta.validation.Validator
import kr.co.kimga.member.domain.dto.CreateMemberRequestDto
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CreateMemberRequestDtoValidationTest {

    private lateinit var validator: Validator

    val email = "test@gmail.com"
    val password = "abcd1234!@#$"
    val name = "test"

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    @DisplayName("유효한 값은 에러가 없다")
    fun `valid values dose not have errors`() {
        // given
        val dto = CreateMemberRequestDto(
            email = email,
            password = password,
            name = name
        )

        // when
        val violations = validator.validate(dto)

        // then
        assertTrue(violations.isEmpty())
    }
}