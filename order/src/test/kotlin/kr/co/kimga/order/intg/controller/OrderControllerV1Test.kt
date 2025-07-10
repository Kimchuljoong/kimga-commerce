package kr.co.kimga.order.intg.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderItemDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderPayDto
import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant
import kotlin.math.floor

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerV1Test {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @BeforeAll
    fun initOnce() {

    }

    @Test
    @DisplayName("주문을 할 수 있다")
    fun `can order`() {

        val requestCreateOrderDto = makeRequestCreateOrderDto(productId = 1L, payMethod = PayMethod.CARD)

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestCreateOrderDto)
        }.andExpect {
            status { isCreated() }
            content {
                string(Matchers.matchesRegex("\\d+"))
            }
        }
    }

    private fun makeRequestCreateOrderDto(productId: Long, payMethod: PayMethod): RequestCreateOrderDto {
        val productName = "테스트 상품"
        val price = 10000.0
        val vat = getVat(price)
        val quantity = 3L

        val orderItems = listOf(
            RequestCreateOrderItemDto(
                productId = productId,
                productName = productName,
                price = price,
                vat = vat,
                quantity = quantity
            ),
        )

        val memberId = 1L
        val provider = "TOSS"
        val amount = orderItems.sumOf { it.price }

        val orderPays = listOf(
            RequestCreateOrderPayDto(
                provider = provider,
                payMethod = payMethod,
                discountAmount = 0.0,
                amount = amount,
                vat = getVat(amount),
                status = null,
            )
        )

        val requestCreateOrderDto = RequestCreateOrderDto(
            memberId = memberId,
            orderDate = Instant.now(),
            orderPays = orderPays,
            orderItems = orderItems,
        )
        return requestCreateOrderDto
    }

    fun getVat(price: Double): Double {
        return floor(price / 1.1 / 10)
    }
}