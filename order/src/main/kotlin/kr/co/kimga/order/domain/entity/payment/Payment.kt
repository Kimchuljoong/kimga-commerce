package kr.co.kimga.order.domain.entity.payment

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.payment.enums.PaymentStatus
import java.time.Instant

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val orderId: Long? = null,

    val amount: Long = 0,

    @Column(nullable = false)
    val status: PaymentStatus? = null,

    @Column(nullable = false)
    val transactionId: String? = null,

    val approvedAt: Instant? = Instant.now()
)