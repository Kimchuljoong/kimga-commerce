package kr.co.kimga.order.intg

import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.order.enums.PayStatus
import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.domain.exception.stock.CanNotAvailableInventory
import kr.co.kimga.order.infrastructure.exception.stock.CanNotFindStock
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderItemDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderPayDto
import org.junit.jupiter.api.*
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
    private lateinit var stockJpaRepository: StockJpaRepository

    @BeforeAll
    fun initOnce() {
        stockJpaRepository.save(
            Stock(
                productId = 1L,
                orderedInventory = 3L,
                totalInventory = 10L,
            )
        )

        stockJpaRepository.save(
            Stock(
                productId = 3L,
            )
        )
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

    @Test
    @DisplayName("재고가 없는 상품은 주문할 수 없다")
    fun `can not order when product stock not exist`() {

        // given
        val productId = 2L
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
        // then
        assertThrows<CanNotFindStock> { orderFacade.createOrder(requestCreateOrderDto) }
    }

    @Test
    @DisplayName("재고가 0인 상품은 주문할 수 없다")
    fun `can not order when product stock is empty`() {

        // given
        val requestCreateOrderDto = makeRequestCreateOrderDto(productId = 3L)

        // when
        // then
        assertThrows<CanNotAvailableInventory> { orderFacade.createOrder(requestCreateOrderDto) }
    }

    private fun makeRequestCreateOrderDto(productId: Long): RequestCreateOrderDto {
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
        return requestCreateOrderDto
    }

    fun getVat(price: Double): Double {
        return floor(price / 1.1 / 10)
    }

    @Test
    @DisplayName("주문을 취소할 수 있다")
    fun `can cancel order`() {
        // given
        val requestCreateOrderDto = makeRequestCreateOrderDto(productId = 1L)
        val orderId = orderFacade.createOrder(requestCreateOrderDto)

        // when
        orderFacade.cancelOrder(orderId)
        val findOrderDetails = orderFacade.findOrderDetails(orderId)

        // then
        assertNotNull(findOrderDetails)
        assertEquals(OrderStatus.CANCELLED, findOrderDetails.orderStatus)
        findOrderDetails.pays.forEach {
            assertEquals(PayStatus.REFUNDED, it.paymentStatus)
        }
    }


}