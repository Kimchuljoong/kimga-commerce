package kr.co.kimga.product.infrastructure.service.dto

data class ModifyProductDto (
    val productId: Long,
    val productName: String,
    val price: Double,
)
