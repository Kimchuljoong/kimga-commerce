package kr.co.kimga.order.infrastructure.service.order.dto

import java.time.Instant

data class RequestCreateOrderDto(
    val memberId: Long,
    val orderDate: Instant,
    val orderPays: List<RequestCreateOrderPayDto>,
    val orderItems: List<RequestCreateOrderItemDto>,
)
