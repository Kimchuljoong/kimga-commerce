package kr.co.kimga.member.infrastructure.repository

import kr.co.kimga.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberJpaRepository : JpaRepository<Member, Long> {

    fun existsByEmail(email: String): Boolean

    fun findByEmailAndPassword(email: String, password: String): Member?
}