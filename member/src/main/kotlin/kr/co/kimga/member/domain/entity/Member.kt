package kr.co.kimga.member.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kr.co.kimga.member.domain.dto.ModifyMemberRequestDto


@Entity
data class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String = "",
    val password: String = "",
    var name: String = ""
) {
    fun modify(modifyRequest: ModifyMemberRequestDto) {
        name = modifyRequest.name
    }
}