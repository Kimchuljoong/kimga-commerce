package kr.co.kimga.order.domain.entity.payment

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.payment.enums.PaymentStatus
import kr.co.kimga.order.infrastructure.service.payment.enums.ActionType
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import java.time.Instant

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val orderId: Long? = null,

    @Column(nullable = false)
    val actionType: ActionType? = null,

    @Column(nullable = false)
    val provider: PaymentProvider? = null,

    val paymentType: PaymentType? = null,

    val amount: Double = 0.0,

    @Column(nullable = false)
    val status: PaymentStatus? = null,

    @Column(nullable = false)
    val transactionId: String? = null,

    val approvedAt: Instant? = Instant.now()
)