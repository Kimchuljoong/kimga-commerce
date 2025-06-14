package kr.co.kimga.product.application

import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.service.ProductService
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ProductFacade (
    private val productService: ProductService
) {

    fun findProducts(productName: String, productStatus: ProductStatus?, pageable: Pageable) =
        productService.findProducts(productName, productStatus, pageable)

    fun findProduct(productId: Long) = productService.findProduct(productId)

    @Transactional
    fun enrollProduct(enrollProductDto: EnrollProductDto) = productService.enrollProduct(enrollProductDto)

    @Transactional
    fun updateProduct(modifyProductDto: ModifyProductDto) = productService.updateProduct(modifyProductDto)

    @Transactional
    fun saleProduct(productId: Long) = productService.saleProduct(productId)

    @Transactional
    fun closeProduct(productId: Long) = productService.closeProduct(productId)
}