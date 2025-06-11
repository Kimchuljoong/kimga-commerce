package kr.co.kimga.product.infrastructure.service

import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ProductService (
    private val productQuerydslRepository: ProductQuerydslRepository
) {

    fun findProductsByProductStatus(productName: String, pageable: Pageable) =
        productQuerydslRepository.findProductsOnSale(productName, pageable)

}