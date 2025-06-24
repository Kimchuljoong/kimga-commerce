package kr.co.kimga.order.infrastructure.service.dto

import java.time.Instant

data class RequestCreateOrderDto(
    val memberId: Long,
    val orderDate: Instant,
    val orderPays: List<RequestCreateOrderPayDto>,
    val orderItems: List<RequestCreateOrderItemDto>,
)
