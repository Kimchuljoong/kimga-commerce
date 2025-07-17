package kr.co.kimga.order.domain.entity.order

import jakarta.persistence.*
import kr.co.kimga.order.domain.exception.order.CanNotCancelItem
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "orderitems")
class OrderItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,

    @Column(nullable = false)
    val productId: Long? = null,

    val productName: String = "",
    val price: Double = 0.0,
    val vat: Double = 0.0,

    val quantity: Long = 0,

    var canceledQuantity: Long = 0,

    @CreatedDate
    val createdAt: Instant? = null,

    @LastModifiedDate
    val modifiedAt: Instant? = null
) {
    fun cancel(quantity: Long) {
        if (canceledQuantity + quantity > this.quantity)
            throw CanNotCancelItem("취소 수량을 초과했습니다")
        canceledQuantity += quantity
    }

    fun isFullyCanceled(): Boolean {
        return quantity == canceledQuantity
    }

    fun remainQuantity(): Long {
        return quantity - canceledQuantity
    }
}