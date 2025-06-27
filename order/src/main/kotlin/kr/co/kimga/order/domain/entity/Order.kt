package kr.co.kimga.order.domain.entity

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.enums.OrderStatus
import kr.co.kimga.order.domain.exception.CanNotChangeOrderStatus
import kr.co.kimga.order.infrastructure.exception.CanNotCreateOrderException
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener::class)
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val memberId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.ORDERED,

    @Column(nullable = false)
    val orderDate: Instant = Instant.now(),

    @OneToMany(mappedBy= "order",  fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    val orderPays: MutableList<OrderPay> = mutableListOf(),

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    val orderItems: MutableList<OrderItem> = mutableListOf(),

    @CreatedDate
    val createdAt: Instant? = null,

    @LastModifiedDate
    val modifiedAt: Instant? = null
) {
    companion object {
        fun of(requestCreateOrderDto: RequestCreateOrderDto): Order {
            if (requestCreateOrderDto.orderPays.isEmpty()) {
                throw CanNotCreateOrderException()
            }

            if (requestCreateOrderDto.orderItems.isEmpty()) {
                throw CanNotCreateOrderException()
            }

            val orderPays = requestCreateOrderDto.orderPays.map {
                OrderPay(
                    payMethod = it.payMethod,
                    discountAmount = it.discountAmount,
                    amount = it.amount,
                    status = it.status,
                )
            }.toMutableList()

            val orderItems = requestCreateOrderDto.orderItems.map {
                OrderItem(
                    productId = it.productId,
                    productName = it.productName,
                    price = it.price,
                    quantity = it.quantity,
                )
            }.toMutableList()

            return Order(
                memberId = requestCreateOrderDto.memberId,
                orderDate = requestCreateOrderDto.orderDate,
                orderPays = orderPays,
                orderItems = orderItems
            )
        }
    }

    fun cancel() {
        if (status !in listOf(OrderStatus.ORDERED, OrderStatus.PAID))
            throw CanNotChangeOrderStatus()

        status = OrderStatus.CANCELLED
    }
}