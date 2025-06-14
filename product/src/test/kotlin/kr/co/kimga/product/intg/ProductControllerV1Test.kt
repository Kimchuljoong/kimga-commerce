package kr.co.kimga.product.intg

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.repository.ProductJpaRepository
import kr.co.kimga.product.interfaces.controller.dto.ProductEnrollRequest
import kr.co.kimga.product.interfaces.controller.dto.ProductUpdateRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerV1Test {

    @Autowired
    lateinit var productRepository: ProductJpaRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val products = listOf(
        Product(productName = "초코 바나나", productStatus = ProductStatus.SALE, price = 10_000.0),
        Product(productName = "딸기 바나나", productStatus = ProductStatus.SALE, price = 20_000.0),
        Product(productName = "사과 음료수", productStatus = ProductStatus.CLOSE, price = 30_000.0),
        Product(productName = "초코 아이스크림", productStatus = ProductStatus.CLOSE, price = 54_000.0),
        Product(productName = "딸기 과자", productStatus = ProductStatus.SALE, price = 77_000.0),
        Product(productName = "딸기 음료수", productStatus = ProductStatus.WAIT, price = 40_000.0),
    )

    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()
        productRepository.saveAll(products)
        productRepository.flush()
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다")
    fun `can get product list`() {
        // when
        // then
        mockMvc.get("/api/v1/product/products") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.content.length()") { value(products.size) }
        }
    }

    @Test
    @DisplayName("특정 이름을 가진 상품 목록을 조회할 수 있다")
    fun `can get product with specific word list`() {

        // given
        val searchWord = "딸기"

        // when
        // then
        mockMvc.get("/api/v1/product/products") {
            param("productName", searchWord)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.content.length()") { value(3) }
        }
    }

    @Test
    @DisplayName("특정 상태를 가진 상품 목록을 조회할 수 있다")
    fun `can get product with specific status list`() {

        // given
        val status = ProductStatus.CLOSE.name

        // when
        // then
        mockMvc.get("/api/v1/product/products") {
            param("productStatus", status)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.content.length()") { value(2) }
        }
    }

    @Test
    @DisplayName("특정 이름과 상태를 가진 상품 목록을 조회할 수 있다")
    fun `can get product with specific word and status list`() {

        // given
        val searchWord = "초코"
        val status = ProductStatus.SALE.name

        // when
        // then
        mockMvc.get("/api/v1/product/products") {
            param("productName", searchWord)
            param("productStatus", status)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.content.length()") { value(1) }
        }
    }

    @Test
    @DisplayName("상품 정보를 조회할 수 있다")
    fun `can get product info`() {

        // given
        val targetProductId = 1L

        // when
        // then
        mockMvc.get("/api/v1/product/$targetProductId") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.productName") { value(products[0].productName) }
            jsonPath("$.productPrice") { value(products[0].price) }
        }
    }

    @Test
    @DisplayName("상품을 신규로 등록 할 수 있다")
    fun `can enroll new product`() {

        // given
        val productName = "테스트 상품1"
        val price = 100_000.0

        val productEnrollRequest = ProductEnrollRequest(
            productName = productName,
            price = price
        )

        // when
        // then
        mockMvc.post("/api/v1/product") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productEnrollRequest)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @DisplayName("상품 정보를 수정 할 수 있다")
    fun `can update product`() {

        // given
        val targetProductId = 1L
        val productName = "테스트 상품2"
        val price = 300_000.0

        val productUpdateRequest = ProductUpdateRequest(
            productName = productName,
            price = price
        )

        // when
        // then
        mockMvc.put("/api/v1/product/$targetProductId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productUpdateRequest)
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/product/$targetProductId") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.productName") { value(productName) }
            jsonPath("$.productPrice") { value(price) }
        }
    }

    @Test
    @DisplayName("없는 상품 정보를 수정 할 수 없다")
    fun `can not update not exist product`() {

        // given
        val targetProductId = 10L
        val productName = "테스트 상품2"
        val price = 300_000.0

        val productUpdateRequest = ProductUpdateRequest(
            productName = productName,
            price = price
        )

        // when
        // then
        mockMvc.put("/api/v1/product/$targetProductId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productUpdateRequest)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("대기 상품을 판매중으로 변경할 수 있다")
    fun `can update product status to SALE`() {

        // given
        val targetProductId = 6L

        // when
        // then
        val beforeProduct = productRepository.findById(targetProductId).get()
        assertEquals(products[5].productStatus, beforeProduct.productStatus)

        mockMvc.post("/api/v1/product/$targetProductId/sale") {
        }.andExpect {
            status { isOk() }
        }

        val afterProduct = productRepository.findById(targetProductId).get()
        assertEquals(ProductStatus.SALE, afterProduct.productStatus)
    }

    @Test
    @DisplayName("대기 상품이 아니면 판매중으로 변경할 수 없다")
    fun `can not update product to SALE when it's status does not WAIT`() {

        // given
        val targetProductId = 3L

        // when
        // then
        val beforeProduct = productRepository.findById(targetProductId).get()
        assertEquals(products[2].productStatus, beforeProduct.productStatus)

        mockMvc.post("/api/v1/product/$targetProductId/sale") {
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("상품을 중지로 변경할 수 있다")
    fun `can update product status to CLOSE`() {

        // given
        val targetProductId = 6L

        // when
        // then
        val beforeProduct = productRepository.findById(targetProductId).get()
        assertEquals(products[5].productStatus, beforeProduct.productStatus)

        mockMvc.post("/api/v1/product/$targetProductId/close") {
        }.andExpect {
            status { isOk() }
        }

        val afterProduct = productRepository.findById(targetProductId).get()
        assertEquals(ProductStatus.CLOSE, afterProduct.productStatus)
    }
}