package kr.co.kimga.order.intg

import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.infrastructure.service.order.OrderService
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderItemDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderPayDto
import kr.co.kimga.order.infrastructure.service.stock.StockService
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.time.Instant
import kotlin.math.floor
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Import(MockPaymentProcessorConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderFacadeTest {

    @Autowired
    private lateinit var orderFacade: OrderFacade

    @Autowired
    private lateinit var stockService: StockService

    @BeforeAll
    fun initOnce() {
        val requestCreateStockDto = RequestCreateStockDto(
            productId = 1L,
        )
        stockService.createStock(requestCreateStockDto)
        stockService.applyInventory(productId = 1L, 10)
    }

    @Test
    @DisplayName("주문을 할 수 있다")
    fun `can order`() {

        // given
        val productId = 1L
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

        val memberId  = 1L
        val provider = "TOSS"
        val payMethod = PayMethod.CARD
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

        // when
        val createdOrderId = orderFacade.createOrder(requestCreateOrderDto)
        val findOrderDetails = orderFacade.findOrderDetails(createdOrderId)

        // then
        assertNotNull(findOrderDetails)
        assertEquals(requestCreateOrderDto.orderItems.size, findOrderDetails.items.size)
        assertEquals(requestCreateOrderDto.orderPays.sumOf { it.amount }, findOrderDetails.payedAmount)

    }

    fun getVat(price: Double): Double {
        return floor(price / 1.1 / 10)
    }
}