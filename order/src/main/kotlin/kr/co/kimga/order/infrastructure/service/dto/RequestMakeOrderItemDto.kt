package kr.co.kimga.order.infrastructure.service.dto

data class RequestMakeOrderItemDto(
    val productId: Long,
    val productName: String,
    val price: Double,
    val vat: Double,
    val quantity: Long,
)