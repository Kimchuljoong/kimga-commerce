package kr.co.kimga.order.domain.entity

import jakarta.persistence.*
import kr.co.kimga.order.domain.entity.enums.OrderStatus
import kr.co.kimga.order.domain.exception.CanNotChangeOrderStatus
import kr.co.kimga.order.infrastructure.exception.CanNotCreateOrderException
import kr.co.kimga.order.infrastructure.service.dto.RequestMakeOrderDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener::class)
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val memberId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PROCESSING,

    val orderDate: Instant = Instant.now(),

    @OneToMany(mappedBy= "order",  fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    val orderPays: MutableList<OrderPay> = mutableListOf(),

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    val items: MutableList<OrderItem> = mutableListOf(),

    @CreatedDate
    val createdAt: Instant? = null,

    @LastModifiedDate
    val modifiedAt: Instant? = null
) {
    companion object {
        fun of(requestMakeOrderDto: RequestMakeOrderDto): Order {

            if (requestMakeOrderDto.orderPays.isEmpty()) {
                throw CanNotCreateOrderException()
            }

            if (requestMakeOrderDto.orderItems.isEmpty()) {
                throw CanNotCreateOrderException()
            }

            val orderPays = requestMakeOrderDto.orderPays.map {
                OrderPay(
                    payMethod = it.payMethod,
                    discountAmount = it.discountAmount,
                    amount = it.amount,
                    vat = it.vat,
                    status = it.status,
                )
            }.toMutableList()

            val orderItems = requestMakeOrderDto.orderItems.map {
                OrderItem(
                    productId = it.productId,
                    productName = it.productName,
                    price = it.price,
                    vat = it.vat,
                    quantity = it.quantity,
                )
            }.toMutableList()

            return Order(
                memberId = requestMakeOrderDto.memberId,
                orderDate = requestMakeOrderDto.orderDate,
                orderPays = orderPays,
                items = orderItems
            )
        }
    }

    fun cancel() {
        if (status !in listOf(OrderStatus.PROCESSING, OrderStatus.PAID)) {
            throw CanNotChangeOrderStatus()
        }
        status = OrderStatus.CANCELLED
    }
}