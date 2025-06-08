package kr.co.kimga.member.domain.service

import kr.co.kimga.member.domain.dto.AuthenticatedMemberDto
import kr.co.kimga.member.domain.exception.MemberNotFoundException
import kr.co.kimga.member.infrastructure.repository.MemberJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class MemberAuthService (
    private val memberRepository: MemberJpaRepository
) {

    fun authenticate(email: String, password: String): AuthenticatedMemberDto {
        val member = memberRepository.findByEmailAndPasswordAndWithdrawYn(email, password) ?: throw MemberNotFoundException()
        return AuthenticatedMemberDto.of(member)
    }
}