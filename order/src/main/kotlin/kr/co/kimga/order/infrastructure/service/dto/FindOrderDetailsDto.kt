package kr.co.kimga.order.infrastructure.service.dto

import kr.co.kimga.order.domain.entity.enums.OrderStatus
import java.time.Instant

data class FindOrderDetailsDto(
    val orderId: Long,
    val orderStatus: OrderStatus,
    val orderDate: Instant,
    val items: List<FindOrderItemDto>,
    val payedAmount: Double,
    val discountAmount: Double,
    val totalAmount: Double,
)
