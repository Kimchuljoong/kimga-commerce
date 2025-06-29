package kr.co.kimga.order.infrastructure.service.dto.order

data class RequestCreateOrderItemDto(
    val productId: Long,
    val productName: String,
    val price: Double,
    val vat: Double,
    val quantity: Long,
)