package kr.co.kimga.product.unit

import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@DataJpaTest
@Import(ProductQuerydslRepository::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryTest @Autowired constructor(
    private val productQuerydslRepository: ProductQuerydslRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val testEntityManager: TestEntityManager
) {

    private val products = listOf(
        Product(productName = "상품1", productStatus = ProductStatus.SALE, price = 10_000.0),
        Product(productName = "상품2", productStatus = ProductStatus.SALE, price = 20_000.0),
        Product(productName = "상품3", productStatus = ProductStatus.CLOSE, price = 30_000.0),
        Product(productName = "상품4", productStatus = ProductStatus.SALE, price = 54_000.0),
        Product(productName = "상품5", productStatus = ProductStatus.SALE, price = 77_000.0),
        Product(productName = "상품6", productStatus = ProductStatus.WAIT, price = 40_000.0),
    )

    @BeforeEach
    @Transactional
    fun setUp() {
        val entityManager = testEntityManager.entityManager
        entityManager.createQuery("DELETE FROM Product").executeUpdate()
        entityManager.flush()
        entityManager.clear()
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

    @Test
    @DisplayName("상품을 신규로 등록할 수 있다")
    fun `can enroll new product`() {

        // given
        val newProduct = Product(productName = "새로운 상품", productStatus = ProductStatus.SALE, price = 10_000.0)

        // when
        val savedProduct = productJpaRepository.save(newProduct)
        productJpaRepository.flush()

        // then
        assertNotNull(savedProduct)
        assertNotNull(savedProduct.id)
    }

}