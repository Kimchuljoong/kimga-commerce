package kr.co.kimga.product.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.entity.QProduct.product
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import kr.co.kimga.product.infrastructure.service.ProductService
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class ProductServiceTest {

    @MockK
    lateinit var productQuerydslRepository: ProductQuerydslRepository

    @MockK
    lateinit var productRepository: ProductJpaRepository

    @InjectMockKs
    lateinit var productService: ProductService


    private val products = listOf(
        Product(productName = "상품1", productStatus = ProductStatus.SALE, price = 10_000.0),
        Product(productName = "상품2", productStatus = ProductStatus.SALE, price = 20_000.0),
        Product(productName = "상품3", productStatus = ProductStatus.CLOSE, price = 30_000.0),
        Product(productName = "상품4", productStatus = ProductStatus.SALE, price = 54_000.0),
        Product(productName = "상품5", productStatus = ProductStatus.SALE, price = 77_000.0),
        Product(productName = "상품6", productStatus = ProductStatus.WAIT, price = 40_000.0),
    )

    @Test
    @DisplayName("상품을 등록할 수 있다")
    fun `can enroll product`() {

        // given
        val productName = "테스트 상품"
        val productPrice = 190_000.0
        val enrollProductDto = EnrollProductDto(productName = productName, price = productPrice)

        val fakeProduct = Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.SALE, price = 200_000.0)

        every {
            productRepository.save(any())
        } returns fakeProduct

        // when
        // then
        assertDoesNotThrow { productService.enrollProduct(enrollProductDto) }
    }

}