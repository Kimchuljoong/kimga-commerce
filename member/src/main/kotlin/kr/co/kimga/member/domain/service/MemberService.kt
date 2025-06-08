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
    fun create(@Valid createMemberDto: CreateMemberDto) : CreatedMemberDto {
        val createRequest = createMemberDto.toEntity()

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
    fun modify(@Valid modifyMemberDto: ModifyMemberDto) : ModifiedMemberDto {
        val member = memberRepository.findById(modifyMemberDto.id)
            .orElseThrow { MemberNotFoundException() }

        member.modify(modifyMemberDto)
        return ModifiedMemberDto.of(member)
    }

    @Transactional
    fun withDraw(withdrawMemberDto: WithdrawMemberDto): WithdrawedMemberDto {
        val member = memberRepository.findById(withdrawMemberDto.id)
            .orElseThrow { MemberNotFoundException() }
        member.withdrawal()
        return WithdrawedMemberDto.of(member)
    }
}