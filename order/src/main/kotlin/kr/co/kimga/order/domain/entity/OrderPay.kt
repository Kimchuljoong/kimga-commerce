package kr.co.kimga.order.domain.entity

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.enums.PayMethod
import kr.co.kimga.order.domain.entity.enums.PayStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "orderpays")
data class OrderPay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val payMethod: PayMethod? = null,
    val amount: Double = 0.0,
    val vat: Double = 0.0,

    @Column(nullable = false)
    val status: PayStatus? = null,

    @CreatedDate
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    val modifiedAt: Instant = Instant.now(),
)