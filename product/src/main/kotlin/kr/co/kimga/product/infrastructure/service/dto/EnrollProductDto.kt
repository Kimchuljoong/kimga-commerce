package kr.co.kimga.product.infrastructure.service.dto

import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.entity.enums.ProductStatus

data class EnrollProductDto(
    val productName: String,
    val productStatus: ProductStatus = ProductStatus.WAIT,
    val price: Double = 0.0,
) {
    fun toEntity(): Product {
        return Product(
            productName = productName,
            productStatus = productStatus,
            price = price
        )
    }
}