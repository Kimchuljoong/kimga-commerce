package kr.co.kimga.order.infrastructure.service.dto

import kr.co.kimga.order.domain.entity.enums.OrderStatus
import java.time.Instant

data class FindOrderDetails(
    val orderId: Long,
    val orderStatus: OrderStatus,
    val orderDate: Instant,
    // todo
)
