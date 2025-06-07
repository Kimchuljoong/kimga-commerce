package kr.co.kimga.member.domain.service

import jakarta.validation.Valid
import kr.co.kimga.member.domain.dto.*
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.domain.exception.MemberDuplicatedException
import kr.co.kimga.member.domain.exception.MemberNotFoundException
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class MemberService (
    private val memberRepository: MemberJpaRepository
) {

    @Transactional
    fun create(@Valid createMemberRequestDto: CreateMemberRequestDto) : CreatedMemberDto {
        val createRequest = createMemberRequestDto.toEntity()

        validateDuplicateMemberByEmail(createRequest)

        val createdMember = memberRepository.save(createRequest)
        return CreatedMemberDto.of(createdMember)

    }

    private fun validateDuplicateMemberByEmail(createRequest: Member) {
        if (memberRepository.existsByEmail(createRequest.email)) {
            throw MemberDuplicatedException("이미 가입한 이메일 입니다")
        }
    }

    @Transactional
    fun modify(@Valid modifyMemberRequestDto: ModifyMemberRequestDto) : ModifiedMemberDto {
        val member = memberRepository.findById(modifyMemberRequestDto.id)
            .orElseThrow { MemberNotFoundException() }

        member.modify(modifyMemberRequestDto)
        return ModifiedMemberDto.of(member)
    }

    @Transactional
    fun withDraw(withdrawMemberRequestDto: WithdrawMemberRequestDto): WithdrawMemberDto {
        val member = memberRepository.findById(withdrawMemberRequestDto.id)
            .orElseThrow { MemberNotFoundException() }
        member.withdrawal()
        return WithdrawMemberDto.of(member)
    }
}