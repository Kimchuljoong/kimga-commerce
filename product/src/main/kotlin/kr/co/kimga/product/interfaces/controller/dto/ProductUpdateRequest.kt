package kr.co.kimga.product.interfaces.controller.dto

import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Length

data class ProductUpdateRequest(
    @field:Length(min = 1, max = 100)
    val productName: String,
    @field:Min(1)
    val price: Double,
)