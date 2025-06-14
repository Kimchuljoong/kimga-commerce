package kr.co.kimga.member.domain.entity

import jakarta.persistence.*
import kr.co.kimga.member.domain.entity.enums.AccountType
import kr.co.kimga.member.domain.exception.CanNotDecreaseAmount
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val memberId: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val accountType: AccountType? = null,

    var balance: Double = 0.0,

    @CreatedDate
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    val modifiedAt: Instant = Instant.now(),
) {
    fun increaseAmount(amount: Double) {
        balance += amount
    }

    fun decreaseAmount(amount: Double) {
        if (balance < amount) {
            throw CanNotDecreaseAmount()
        }
        balance -= amount
    }
}