package kr.co.kimga.product.interfaces.controller

import jakarta.validation.Valid
import kr.co.kimga.product.application.ProductFacade
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import kr.co.kimga.product.infrastructure.service.dto.ProductDto
import kr.co.kimga.product.interfaces.controller.dto.ProductEnrollRequest
import kr.co.kimga.product.interfaces.controller.dto.ProductUpdateRequest
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
class ProductControllerV1(
    private val productFacade: ProductFacade
) {

    @GetMapping("/products")
    fun getProducts(
        @RequestParam(required = false) productName: String = "",
        @RequestParam(required = false) productStatus: ProductStatus?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<ProductDto> {
        val safePage = page.coerceAtLeast(0)
        val safeSize = size.coerceIn(10, 50)
        val pageable = PageRequest.of(safePage, safeSize)
        return productFacade.findProducts(productName, productStatus, pageable)
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") productId: Long) = productFacade.findProduct(productId)

    @PostMapping("")
    fun enrollProduct(
        @RequestBody @Valid productEnrollRequest: ProductEnrollRequest
    ) = productFacade.enrollProduct(
        EnrollProductDto(productName = productEnrollRequest.productName, price = productEnrollRequest.price)
    )

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: Long,
        @RequestBody @Valid productUpdateRequest: ProductUpdateRequest
    ) = productFacade.updateProduct(
        ModifyProductDto(productId = productId, productName = productUpdateRequest.productName, price = productUpdateRequest.price)
    )

    @PostMapping("/{productId}/sale")
    fun saleProduct(
        @PathVariable("productId") productId: Long
    ) = productFacade.saleProduct(productId)

    @PostMapping("/{productId}/close")
    fun closeProduct(
        @PathVariable("productId") productId: Long
    ) = productFacade.closeProduct(productId)
}