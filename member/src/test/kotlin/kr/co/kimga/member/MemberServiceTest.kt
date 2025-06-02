package kr.co.kimga.member

import kr.co.kimga.member.domain.dto.CreateMemberRequestDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MemberServiceTest {

    @Test
    @DisplayName("junit 정상 동작 테스트")
    fun emptyTest() {}


    @Test
    @DisplayName("CreateMemberRequestDto를 통해 MemberEntity 생성를 생성할 수 있다")
    fun `toEntity should convert dto to entity`() {
        // given
        val email = "test@test.com"
        val password = "aa123"
        val name = "tester"

        val dto = CreateMemberRequestDto(
            email = email,
            password = password,
            name = name
        )

        // when
        val entity = dto.toEntity()

        // then
        assertThat(entity.email).isEqualTo(email)
        assertThat(entity.password).isEqualTo(password)
        assertThat(entity.name).isEqualTo(name)
        assertThat(entity.id).isNull()
    }

    @Test
    @DisplayName("CreateMemberRequestDto Validation을 확인한다")
    fun `validate CreateMemberRequestDto`() {
        // given

        // when

        // then
    }

}