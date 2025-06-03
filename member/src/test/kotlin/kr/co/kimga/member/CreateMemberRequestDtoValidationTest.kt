package kr.co.kimga.member

import jakarta.validation.Validation
import jakarta.validation.Validator
import kr.co.kimga.member.domain.dto.CreateMemberRequestDto
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CreateMemberRequestDtoValidationTest {

    private lateinit var validator: Validator

    private val email = "test@gmail.com"
    private val password = "abcd1234!@#$"
    private val name = "test"

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    @DisplayName("유효한 값은 에러가 없다")
    fun `valid values dose not have errors`() {
        // given
        val dto = CreateMemberRequestDto(email = email, password = password, name = name)

        // when
        val violations = validator.validate(dto)

        // then
        assertTrue(violations.isEmpty())
    }

    @Test
    @DisplayName("이메일 값을 비어있으면 안된다")
    fun `email value must not be empty`() {

        val emptyEmail = ""

        // given
        val dto = CreateMemberRequestDto(email = emptyEmail, password = password, name = name)

        // when
        val violations = validator.validate(dto)

        // then
        assertFalse(violations.isEmpty())
        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("email", violation.propertyPath.toString())
        assertEquals("이메일은 필수 입니다", violation.message)
    }

    @Test
    @DisplayName("이메일 형식이 틀리면 안된다")
    fun  `email should be valid`() {
        // given
        val invalidEmail = "email"
        val dto = CreateMemberRequestDto(email = invalidEmail, password = password, name = name)

        // when
        val violations = validator.validate(dto)

        // then
        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("email", violation.propertyPath.toString())
        assertEquals("이메일 형식이 올바르지 않습니다", violation.message)
    }

    @Test
    @DisplayName("비밀번호는 10자리 이상이어야 한다")
    fun  `password must be over 10 characters long`() {
        // given
        val emptyPassword = ""
        val dto = CreateMemberRequestDto(email = email, password = emptyPassword, name = name)

        // when
        val violations = validator.validate(dto)

        println(violations)

        // then
        assertEquals(2, violations.size)
        val violationMessages = violations.map { it.message }
        assertTrue("비밀번호는 10자 이상 이어야 합니다" in violationMessages)
        assertTrue("비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다" in violationMessages)
    }

    @Test
    @DisplayName("비밀번호는 15자리를 초과할 수 없다")
    fun  `password must be less 15 characters long`() {
        // given
        val longPassword = "abcd1234!@#$12345"
        val dto = CreateMemberRequestDto(email = email, password = longPassword, name = name)

        // when
        val violations = validator.validate(dto)

        println(violations)

        // then
        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("비밀번호는 15자를 초과할 수 없습니다", violation.message)
    }

    @Test
    @DisplayName("비밀번호는 영문자, 숫자, 특수문자를 포함해야 한다")
    fun `password must contain English letters, numbers, and special characters`() {

        // given
        val noLetterPassword = "1234!@#$123"
        val noNumberPassword = "abcdefgh!@#$"
        val noSpecialCharacterPassword = "abcd1234123"

        val dtoWithNoLetterPassword = CreateMemberRequestDto(email = email, password = noLetterPassword, name = name)
        val dtoWithNoNumberPassword = CreateMemberRequestDto(email = email, password = noNumberPassword, name = name)
        val dtoWithNoSpecialCharacterPassword = CreateMemberRequestDto(email = email, password = noSpecialCharacterPassword, name = name)

        // when
        val violations1 = validator.validate(dtoWithNoLetterPassword)
        val violations2 = validator.validate(dtoWithNoNumberPassword)
        val violations3 = validator.validate(dtoWithNoSpecialCharacterPassword)

        // then
        assertEquals(1, violations1.size)
        assertEquals(1, violations2.size)
        assertEquals(1, violations3.size)
        val violation1 = violations1.first()
        val violation2 = violations2.first()
        val violation3 = violations3.first()
        assertEquals("비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다", violation1.message)
        assertEquals("비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다", violation2.message)
        assertEquals("비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다", violation3.message)
    }

    @Test
    @DisplayName("이름은 2글자 이상이어야 한다")
    fun `name must be over 2 characters long`() {

        // given
        val invalidname = "a"
        val dto = CreateMemberRequestDto(email = email, password = password, name = invalidname)

        // when

        val violations = validator.validate(dto)

        // then
        assertFalse(violations.isEmpty())
        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("name", violation.propertyPath.toString())
        assertEquals("이름은 2글자 이상 작성해야 합니다", violation.message)


    }


}