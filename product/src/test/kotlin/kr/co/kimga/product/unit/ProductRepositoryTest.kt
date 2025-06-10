package kr.co.kimga.product.unit

import jakarta.persistence.EntityManager
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

@DataJpaTest
@Import(ProductQuerydslRepository::class)
class ProductRepositoryTest @Autowired constructor(
    private val productQuerydslRepository: ProductQuerydslRepository,
    private val entityManager: EntityManager
) {

    @BeforeEach
    fun setUp() {
        val products = listOf(
            Product(productName = "상품1", productStatus = ProductStatus.SALE, price = 10_000.0),
            Product(productName = "상품2", productStatus = ProductStatus.SALE, price = 20_000.0),
            Product(productName = "상품3", productStatus = ProductStatus.CLOSE, price = 30_000.0),
            Product(productName = "상품4", productStatus = ProductStatus.SALE, price = 54_000.0),
            Product(productName = "상품5", productStatus = ProductStatus.SALE, price = 77_000.0),
            Product(productName = "상품6", productStatus = ProductStatus.WAIT, price = 40_000.0),
        )
        products.forEach { entityManager.persist(it) }
        entityManager.flush()
        entityManager.clear()
    }

    @Test
    @DisplayName("상품 상태가 판매중인 상품들을 페이지로 조회할 수 있다")
    fun `can fetch products on sales with page`() {

        // given
        val searchProductName = "상품"
        val pageable = PageRequest.of(0, 3)

        // when
        val findProductsOnSale = productQuerydslRepository.findProductsOnSale(searchProductName, pageable)

        // then
        assertEquals(2, findProductsOnSale.totalPages)
        assertEquals(3, findProductsOnSale.content.count())
        assertEquals(5, findProductsOnSale.content.first().id)
    }
}