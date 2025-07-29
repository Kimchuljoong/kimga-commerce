package kr.co.kimga.member.unit

import io.mockk.every
import io.mockk.mockk
import kr.co.kimga.member.domain.dto.CreateMemberDto
import kr.co.kimga.member.domain.dto.ModifyMemberDto
import kr.co.kimga.member.domain.dto.WithdrawMemberDto
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.domain.exception.MemberDuplicatedException
import kr.co.kimga.member.domain.service.MemberService
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MemberServiceTest{

    private lateinit var memberRepository: MemberJpaRepository
    private lateinit var memberService: MemberService

    private val email = "test@test.com"
    private val password = "abcd1234!@#$"
    private val name = "tester"

    @BeforeEach
    fun setUp() {
        memberRepository = mockk()
        memberService = MemberService(memberRepository)
    }

    @Test
    @DisplayName("junit 정상 동작 테스트")
    fun emptyTest() {}

    @Test
    @DisplayName("CreateMemberRequestDto를 통해 MemberEntity 생성를 생성할 수 있다")
    fun `toEntity should convert dto to entity`() {
        // given
        val dto = CreateMemberDto(
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
    @DisplayName("CreateMemberRequestDto를 통해 사용자를 생성할 수 있다")
    fun `Create Member From CreateMemberRequestDto`() {

        // given
        val createMemberDto = CreateMemberDto(
            email = email,
            password = password,
            name = name
        )

        val fakeCreatedMember = Member(
            id = 1,
            email = email,
            password = password,
            name = name
        )

        every { memberRepository.save(any()) } returns fakeCreatedMember
        every { memberRepository.existsByEmail(any()) } returns false

        // when
        val createdMember = memberService.create(createMemberDto)

        // then
        assertThat(createdMember).isNotNull
        assertThat(createdMember.id).isEqualTo(1)
        assertThat(createdMember.email).isEqualTo(email)
        assertThat(createdMember.name).isEqualTo(name)
    }

    @Test
    @DisplayName("동일한 이메일로 중복으로 사용자를 생성할 수 없다")
    fun `Can not create Member from Duplicated Email`() {

        // given
        val createMemberDto = CreateMemberDto(
            email = email,
            password = password,
            name = name
        )

        every {
            memberRepository.existsByEmail(any())
        } returns true

        // when
        // then
        assertThrows<MemberDuplicatedException> {
            memberService.create(createMemberDto)
        }.let {
            assertEquals("이미 가입한 이메일 입니다", it.message)
        }
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있다")
    fun `can modify member info`() {
        val modifyName = "modify_name"
        // given
        val modifyMemberDto = ModifyMemberDto(1, modifyName);

        val fakeModifiedMember = Member(
            id = 1,
            email = email,
            password = password,
            name = modifyName
        )

        every { memberRepository.findById(any()) } returns Optional.of(fakeModifiedMember)

        // when
        val modifiedMember = memberService.modify(modifyMemberDto)

        // then
        assertNotNull(modifiedMember)
        assertEquals(modifyName, modifiedMember.name)
    }

    @Test
    @DisplayName("회원 탈퇴를 할 수 있다")
    fun `can withdraw member`() {

        // given
        val withdrawMemberDto = WithdrawMemberDto(1)
        val fakeMember = Member(1, email, password, name)

        every { memberRepository.findById(any()) } returns Optional.of(fakeMember)

        // when
        val withdrawResult = memberService.withDraw(withdrawMemberDto)

        // then
        assertEquals(true, fakeMember.withdrawYn)
        assertNotNull(fakeMember.withdrawAt)
        assertNotNull(withdrawResult.withdrawDate)
        assertEquals(fakeMember.withdrawAt, withdrawResult.withdrawDate)
    }

    @Test
    @DisplayName("사용자는 가입한 정보로 로그인할 수 있다")
    fun `user can login with their registered info`() {

    }

}