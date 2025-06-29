package kr.co.kimga.order.infrastructure.service.order.dto

import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import java.time.Instant

data class FindOrderDto(
    val orderId: Long,
    val memberId: Long,
    val orderStatus: OrderStatus,
    val orderDate: Instant,
    val amount: Double,
)
