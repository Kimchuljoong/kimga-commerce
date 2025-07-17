package kr.co.kimga.order.infrastructure.service.order.dto

import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import java.time.Instant

data class RequestFindOrdersDto(
    val memberId: Long,
    val orderStatus: OrderStatus?,
    val from: Instant,
    val to: Instant,
)
