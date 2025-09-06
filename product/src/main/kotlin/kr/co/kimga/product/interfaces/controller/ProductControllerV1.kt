package kr.co.kimga.product.interfaces.controller

import kr.co.kimga.product.application.ProductFacade
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import kr.co.kimga.product.infrastructure.service.dto.ProductDto
import kr.co.kimga.product.interfaces.controller.dto.ProductEnrollRequest
import kr.co.kimga.product.interfaces.controller.dto.ProductUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductControllerV1(
    private val productFacade: ProductFacade
) : ProductApiV1 {

    override fun getProducts(
        productName: String,
        productStatus: ProductStatus?,
        page: Int,
        size: Int
    ): Page<ProductDto> {
        val safePage = page.coerceAtLeast(0)
        val safeSize = size.coerceIn(10, 50)
        val pageable = PageRequest.of(safePage, safeSize)
        return productFacade.findProducts(productName, productStatus, pageable)
    }

    override fun getProduct(productId: Long): ProductDto =
        productFacade.findProduct(productId)

    override fun enrollProduct(productEnrollRequest: ProductEnrollRequest) {
        productFacade.enrollProduct(
            EnrollProductDto(
                productName = productEnrollRequest.productName,
                price = productEnrollRequest.price
            )
        )
    }

    override fun updateProduct(productId: Long, productUpdateRequest: ProductUpdateRequest) {
        productFacade.updateProduct(
            ModifyProductDto(
                productId = productId,
                productName = productUpdateRequest.productName,
                price = productUpdateRequest.price
            )
        )
    }

    override fun saleProduct(productId: Long) {
        productFacade.saleProduct(productId)
    }

    override fun closeProduct(productId: Long) {
        productFacade.closeProduct(productId)
    }
}
