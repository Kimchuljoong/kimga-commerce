package kr.co.kimga.member.domain.entity

import jakarta.persistence.*
import kr.co.kimga.member.domain.dto.ModifyMemberRequestDto
import java.time.Instant

@Entity
data class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val email: String = "",

    val password: String = "",
    var name: String = "",
    val createdAt: Instant = Instant.now(),
    var modifiedAt: Instant = Instant.now(),
    var withdrawYn: Boolean = false,
    var withdrawAt: Instant? = null
) {
    fun modify(modifyRequest: ModifyMemberRequestDto) {
        name = modifyRequest.name
        modifiedAt = Instant.now()
    }

    fun withdrawal() {
        if (!withdrawYn) {
            withdrawYn = true

            val now = Instant.now()
            withdrawAt = now
            modifiedAt = now
        }
    }
}