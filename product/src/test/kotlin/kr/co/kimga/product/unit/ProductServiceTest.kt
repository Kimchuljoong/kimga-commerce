package kr.co.kimga.product.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.exception.PriceCanNotChangeException
import kr.co.kimga.product.domain.exception.ProductNotFoundException
import kr.co.kimga.product.domain.exception.ProductStatusCanNotChangeException
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import kr.co.kimga.product.infrastructure.service.ProductService
import kr.co.kimga.product.infrastructure.service.dto.EnrollProductDto
import kr.co.kimga.product.infrastructure.service.dto.ModifyProductDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class ProductServiceTest {

    @MockK
    lateinit var productQuerydslRepository: ProductQuerydslRepository

    @MockK
    lateinit var productRepository: ProductJpaRepository

    @InjectMockKs
    lateinit var productService: ProductService


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
        verify(exactly = 1) { productRepository.save(any()) }
    }

    @Test
    @DisplayName("상품이 정보를 수정할 수 있다")
    fun `product information can be modified`() {

        // given
        val modifyProductName = "변경 상품"
        val modifyProductPrice = 200_000.0

        val modifyProductDto =
            ModifyProductDto(productId = 1L, productName = modifyProductName, price = modifyProductPrice)
        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.SALE, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        productService.updateProduct(modifyProductDto)

        // then
        assertEquals(modifyProductName, fakeProduct.productName)
        assertEquals(modifyProductPrice, fakeProduct.price)
    }

    @Test
    @DisplayName("상품 가격은 0보다 작게 수정할 수 있다")
    fun  `can not modify product price lower then 0`() {

        // given
        val modifyProductName = "변경 상품"
        val modifyProductPrice = -1.0

        val modifyProductDto =
            ModifyProductDto(productId = 1L, productName = modifyProductName, price = modifyProductPrice)
        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.SALE, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        // then
        assertThrows<PriceCanNotChangeException> { productService.updateProduct(modifyProductDto) }
    }

    @Test
    @DisplayName("존재하지 않는 상품에 대해서 수정을 할 수 없다")
    fun `can not modify a product that does not exist`() {

        // given
        val modifyProductName = "변경 상품"
        val modifyProductPrice = 200_000.0

        val modifyProductDto =
            ModifyProductDto(productId = 1L, productName = modifyProductName, price = modifyProductPrice)

        every {
            productRepository.findById(any())
        } returns Optional.empty()

        // when
        // then
        assertThrows<ProductNotFoundException> { productService.updateProduct(modifyProductDto) }
    }

    @Test
    @DisplayName("상품을 판매중으로 변경할 수 있다")
    fun `can modify product status to SALE`() {

        // given
        val productId = 1L

        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.WAIT, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        productService.saleProduct(productId)

        // then
        assertEquals(ProductStatus.SALE, fakeProduct.productStatus)
    }

    @Test
    @DisplayName("대기 상태가 아니면 상품은 판매중으로 변경할 수 없다")
    fun `if product not in WAIT, cannot be changed to SALE`() {

        // given
        val productId = 1L
        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.CLOSE, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        // then
        assertThrows<ProductStatusCanNotChangeException> { productService.saleProduct(productId) }
    }

    @Test
    @DisplayName("상품을 판매 중지 할 수 있다")
    fun `can modify product status to CLOSE`() {

        // given
        val productId = 1L

        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.SALE, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        productService.closeProduct(productId)

        // then
        assertEquals(ProductStatus.CLOSE, fakeProduct.productStatus)
    }

    @Test
    @DisplayName("판매 중지 상태인 상품을 판매 중지 할 수 없다")
    fun `can not modify product status to CLOSE when it is in CLOSE`() {

        // given
        val productId = 1L

        val fakeProduct =
            Product(id = 1L, productName = "테스트 상품", productStatus = ProductStatus.CLOSE, price = 200_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        // then
        assertThrows<ProductStatusCanNotChangeException> { productService.closeProduct(productId) }
    }

    @Test
    @DisplayName("특정 키워드를 포함하는 상품 목록을 조회할 수 있다")
    fun `can find products that contain specific keywords`() {

        // given
        val pageable: Pageable = PageRequest.of(0, 2)
        val fakeProductList = listOf(
            Product(id = 1L, productName = "초코 아이스크림", productStatus = ProductStatus.SALE, price = 10_000.0),
            Product(id = 2L, productName = "초코 바나나", productStatus = ProductStatus.SALE, price = 20_000.0)
        )
        val productPage = PageImpl(fakeProductList, pageable, 2)

        every {
            productQuerydslRepository.findProducts(any(), any(), any())
        } returns productPage

        // when
        val resultProductList = productService.findProducts("초코", ProductStatus.SALE, pageable)

        // then
        assertEquals(2, resultProductList.content.size)
        assertTrue(resultProductList.content[0].productName.contains("초코"))
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다")
    fun `can find products`() {

        // given
        val pageable: Pageable = PageRequest.of(0, 4)
        val fakeProductList = listOf(
            Product(id = 1L, productName = "초코 아이스크림", productStatus = ProductStatus.SALE, price = 10_000.0),
            Product(id = 2L, productName = "초코 바나나", productStatus = ProductStatus.SALE, price = 15_000.0),
            Product(id = 3L, productName = "딸기 바나나", productStatus = ProductStatus.SALE, price = 12_000.0),
            Product(id = 4L, productName = "딸기 음료수", productStatus = ProductStatus.SALE, price = 3000.0)
        )
        val productPage = PageImpl(fakeProductList, pageable, 4)

        every {
            productQuerydslRepository.findProducts(any(), any(), any())
        } returns productPage

        // when
        val resultProductList = productService.findProducts("", ProductStatus.SALE, pageable)

        // then
        assertEquals(4, resultProductList.content.size)
        assertEquals(1, resultProductList.totalPages)
    }

    @Test
    @DisplayName("상품 정보를 조회할 수 있다")
    fun `find product`() {

        // given
        val productId = 2L
        val fakeProduct = Product(id = 2L, productName = "초코 바나나", productStatus = ProductStatus.SALE, price = 15_000.0)

        every {
            productRepository.findById(any())
        } returns Optional.of(fakeProduct)

        // when
        val product = productService.findProduct(productId)

        // then
        assertEquals(productId, product.productId)
    }

}