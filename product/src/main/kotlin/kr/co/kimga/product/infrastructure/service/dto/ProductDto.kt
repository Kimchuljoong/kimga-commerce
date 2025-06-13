package kr.co.kimga.product.infrastructure.service.dto

import kr.co.kimga.product.domain.entity.Product

data class ProductDto(
    val productId: Long,
    val productName: String,
    val productPrice: Double,
) {
    companion object {
        fun of(product: Product) = ProductDto(
            productId = product.id!!,
            productName = product.productName,
            productPrice = product.price
        )
    }
}
