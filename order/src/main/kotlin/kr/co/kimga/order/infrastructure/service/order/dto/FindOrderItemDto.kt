package kr.co.kimga.order.infrastructure.service.order.dto

data class FindOrderItemDto(
    val productId: Long,
    val productName: String,
    val quantity: Long,
)
