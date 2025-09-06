package kr.co.kimga.product.infrastructure.service

import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.exception.ProductNotFoundException
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import kr.co.kimga.product.infrastructure.service.dto.ProductDto
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ProductService (
    private val productQuerydslRepository: ProductQuerydslRepository,
    private val productRepository: ProductJpaRepository
) {

    fun findProducts(productName: String, productStatue: ProductStatus?, pageable: Pageable): Page<ProductDto> {
        val pagedProducts = productQuerydslRepository.findProducts(productName, productStatue, pageable)
        return pagedProducts.map { ProductDto.of(it) }
    }

    fun findProduct(productId: Long): ProductDto {
        val product = productRepository.findById(productId)
            .orElseThrow { throw ProductNotFoundException() }
        return ProductDto.of(product)
    }

    @Transactional
    fun enrollProduct(enrollProductDto: EnrollProductDto) {
        val newProduct = enrollProductDto.toEntity()
        productRepository.save(newProduct)
    }

    @Transactional
    fun saleProduct(productId: Long) {
        val product = productRepository.findById(productId)
            .orElseThrow { throw ProductNotFoundException() }
        product.sale()
    }

    @Transactional
    fun closeProduct(productId: Long) {
        val product = productRepository.findById(productId)
            .orElseThrow { throw ProductNotFoundException() }
        product.close()
    }

    @Transactional
    fun updateProduct(modifyProductDto: ModifyProductDto) {
        val findProduct =
            productRepository.findById(modifyProductDto.productId).orElseThrow { throw ProductNotFoundException() }
        findProduct.changeProductName(modifyProductDto.productName)
        findProduct.changePrice(modifyProductDto.price)
    }

    fun getProductStatus(productId: Long): ProductStatus {
        val findProduct =
            productRepository.findById(productId).orElseThrow { throw ProductNotFoundException() }
        return findProduct.productStatus
    }
}