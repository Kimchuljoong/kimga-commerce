package kr.co.kimga.order.domain.entity.order

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.order.enums.PayStatus
import kr.co.kimga.order.domain.exception.order.CanNotRefund
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "orderpays")
class OrderPay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var payMethod: PayMethod? = null,

    val discountAmount: Double = 0.0,
    val amount: Double = 0.0,
    val canceledDiscountAmount: Double = 0.0,
    var refundedAmount: Double = 0.0,

    @Column(nullable = false)
    val status: PayStatus? = null,

    @CreatedDate
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    val modifiedAt: Instant = Instant.now(),
) {
    fun refund(amount: Double) {
        if (refundedAmount + amount > this.amount)
            throw CanNotRefund("환불 금액을 초과했습니다")
        refundedAmount += amount
    }

    fun refund(amounts: List<Double>) {
        val totalRefundAmount = amounts.sum()
        if (refundedAmount + totalRefundAmount > this.amount)
            throw CanNotRefund("환불 금액을 초과했습니다")
        refundedAmount += totalRefundAmount
    }

    fun isFullyRefunded(): Boolean {
        return amount == refundedAmount
    }

    fun remainAmount(): Double {
        return amount - refundedAmount
    }
}