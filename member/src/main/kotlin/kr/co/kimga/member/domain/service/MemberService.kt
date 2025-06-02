package kr.co.kimga.member.domain.service

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
    fun create() {

    }
}