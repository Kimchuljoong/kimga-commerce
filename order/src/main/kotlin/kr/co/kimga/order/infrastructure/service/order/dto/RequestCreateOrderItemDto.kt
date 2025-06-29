package kr.co.kimga.order.infrastructure.service.order.dto

data class RequestCreateOrderItemDto(
    val productId: Long,
    val productName: String,
    val price: Double,
    val vat: Double,
    val quantity: Long,
)