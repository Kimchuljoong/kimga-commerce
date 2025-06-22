package kr.co.kimga.order.infrastructure.service.dto

import java.time.Instant

data class RequestMakeOrderDto(
    val memberId: Long,
    val orderDate: Instant,
    val orderPays: List<RequestMakeOrderPayDto>,
    val orderItems: List<RequestMakeOrderItemDto>,
)
