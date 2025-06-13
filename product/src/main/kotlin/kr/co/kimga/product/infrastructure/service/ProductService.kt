package kr.co.kimga.product.infrastructure.service

import jakarta.persistence.EntityNotFoundException
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.exception.ProductNotFoundException
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ProductService (
    private val productQuerydslRepository: ProductQuerydslRepository,
    private val productRepository: ProductJpaRepository
) {

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

    fun findProductsByProductStatus(productName: String, pageable: Pageable) =
        productQuerydslRepository.findProductsOnSale(productName, pageable)

}