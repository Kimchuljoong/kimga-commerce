package kr.co.kimga.member.domain.service

import kr.co.kimga.member.domain.dto.CreateMemberRequestDto
import kr.co.kimga.member.domain.dto.CreatedMemberDto
import kr.co.kimga.member.domain.entity.Member
import kr.co.kimga.member.domain.exception.MemberDuplicatedException
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class MemberService (
    val memberJpaRepository: MemberJpaRepository
) {

    @Transactional
    fun create(createMemberRequestDto: CreateMemberRequestDto) : CreatedMemberDto {
        val createRequest = createMemberRequestDto.toEntity()

        validateDuplicateMemberByEmail(createRequest)

        val createdMember = memberJpaRepository.save(createRequest)
        return CreatedMemberDto.from(createdMember)

    }

    private fun validateDuplicateMemberByEmail(createRequest: Member) {
        if (memberJpaRepository.existsByEmail(createRequest.email)) {
            throw MemberDuplicatedException("이미 가입한 이메일 입니다")
        }
    }
}