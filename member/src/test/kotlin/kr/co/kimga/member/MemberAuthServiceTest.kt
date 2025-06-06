package kr.co.kimga.member

import io.mockk.every
import io.mockk.mockk
import kr.co.kimga.member.domain.dto.MemberLoginDto
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.domain.service.MemberAuthService
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MemberAuthServiceTest {

    private lateinit var memberRepository: MemberJpaRepository
    private lateinit var memberAuthService: MemberAuthService

    private val email = "test@test.com"
    private val name = "test"
    private val password = "test1234!@#$"

    @BeforeEach
    fun setUp() {
        memberRepository = mockk()
        memberAuthService = MemberAuthService(memberRepository)
    }

    @Test
    @DisplayName("EMAIL과 PASSWORD로 사용자를 확인한다")
    fun `authenticate member from id and password`() {

        // given
        val memberLoginDto = MemberLoginDto(email, password)
        val fakeMember = Member(1, email, password, name)
        every { memberRepository.findByEmailAndPassword(any(), any()) } returns fakeMember

        // when
        val authenticatedMemberDto = memberAuthService.authenticate(memberLoginDto.email, memberLoginDto.password)

        // then
        assertEquals(1, authenticatedMemberDto.id)
        assertEquals(name, authenticatedMemberDto.name)
    }

}