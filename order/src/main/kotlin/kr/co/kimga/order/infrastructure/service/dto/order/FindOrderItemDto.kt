package kr.co.kimga.order.infrastructure.service.dto.order

data class FindOrderItemDto(
    val productId: Long,
    val productName: String,
    val quantity: Long,
)
