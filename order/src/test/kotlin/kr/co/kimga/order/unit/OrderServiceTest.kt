package kr.co.kimga.order.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.domain.entity.OrderItem
import kr.co.kimga.order.domain.entity.OrderPay
import kr.co.kimga.order.domain.entity.enums.OrderStatus
import kr.co.kimga.order.domain.entity.enums.PayMethod
import kr.co.kimga.order.domain.entity.enums.PayStatus
import kr.co.kimga.order.domain.exception.CanNotChangeOrderStatus
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderQuerydslRepository
import kr.co.kimga.order.infrastructure.service.OrderService
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderItemDto
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderPayDto
import kr.co.kimga.order.infrastructure.service.dto.RequestFindOrdersDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.floor
import kotlin.test.assertEquals


@ExtendWith(MockKExtension::class)
class OrderServiceTest {

    @MockK
    private lateinit var orderJpaRepository: OrderJpaRepository

    @MockK
    private lateinit var orderQuerydslRepository: OrderQuerydslRepository

    @InjectMockKs
    private lateinit var orderService: OrderService

    @Test
    @DisplayName("주문을 생성할 수 있다")
    fun `can create order`() {

        // given
        val memberId = 1L
        val orderDate = Instant.now()
        val amount = 10000.0

        val productId = 2L
        val productName = "테스트1"

        val orderPays = listOf(
            RequestCreateOrderPayDto(
                payMethod =  PayMethod.CARD,
                discountAmount = 0.0,
                amount = amount,
                vat = floor(amount / 1.1 / 10.0),
                status = PayStatus.SUCCEED
            )
        )

        val orderItems = listOf(
            RequestCreateOrderItemDto(
                productId = productId,
                productName = productName,
                quantity = 1,
                price = amount,
                vat = floor(amount / 1.1 / 10.0)
            )
        )

        val requestCreateOrderDto = RequestCreateOrderDto(
            memberId = memberId,
            orderDate = orderDate,
            orderPays = orderPays,
            orderItems = orderItems
        )

        every {
            orderJpaRepository.save(any())
        } returns mockk<Order>()

        // when
        orderService.createOrder(requestCreateOrderDto)

        // then
        verify { orderJpaRepository.save(any()) }
    }

    @Test
    @DisplayName("결제 완료 이전 상태의 주문은 취소할 수 있다")
    fun `can cancel order when status less equal PAID`() {

        // given
        val id = 1L
        val status = OrderStatus.PAID

        val fakeOrder = Order(
            id = id,
            status = status,
        )

        every {
            orderJpaRepository.findById(any())
        } returns Optional.of(fakeOrder)

        // when
        orderService.cancelOrder(id)

        // then
        assertEquals(OrderStatus.CANCELLED, fakeOrder.status)

    }

    @Test
    @DisplayName("배송이 시작된 이후에는 주문을 취소할 수 없다")
    fun `can not cancel order when status over SHIPPED`() {

        // given
        val id = 1L
        val status = OrderStatus.SHIPPED

        val fakeOrder = Order(
            id = id,
            status = status,
        )

        every {
            orderJpaRepository.findById(any())
        } returns Optional.of(fakeOrder)

        // when
        // then
        assertThrows<CanNotChangeOrderStatus> {
            orderService.cancelOrder(id)
        }
    }

    @Test
    @DisplayName("회원의 주문들을 조회할 수 있다")
    fun `can find orders`() {

        // given
        val requestFindOrdersDto = RequestFindOrdersDto(
            memberId = 1L,
            orderStatus = null,
            from = Instant.now().minus(10, ChronoUnit.DAYS),
            to = Instant.now()
        )
        val pageable = PageRequest.of(0, 10)

        val orderPays1 = mutableListOf(
            OrderPay(
                status = PayStatus.SUCCEED,
                amount = 10000.0
            ),
            OrderPay(
                status = PayStatus.SUCCEED,
                amount = 2000.0
            )
        )

        val orderPays2 = mutableListOf(
            OrderPay(
                status = PayStatus.SUCCEED,
                amount = 3000.0
            ),
            OrderPay(
                status = PayStatus.SUCCEED,
                amount = 4000.0
            )
        )

        val fakeOrder = listOf(
            Order(
                id = 1L,
                memberId = 1L,
                status = OrderStatus.ORDERED,
                orderDate = Instant.now(),
                orderPays = orderPays1
            ),
            Order(
                id = 2L,
                memberId = 1L,
                status = OrderStatus.PAID,
                orderDate = Instant.now().minus(3, ChronoUnit.DAYS),
                orderPays = orderPays2
            )
        )

        every {
            orderQuerydslRepository.findOrders(any(), any(), any(), any(), any())
        } returns PageImpl(fakeOrder, pageable, 2)

        // when
        val findOrders = orderService.findOrders(requestFindOrdersDto, pageable)

        // then
        assertEquals(2, findOrders.numberOfElements)
        assertEquals(1, findOrders.totalPages)
        assertEquals(12000.0, findOrders.content[0].amount)
        assertEquals(7000.0, findOrders.content[1].amount)

    }

    @Test
    @DisplayName("주문 상세 정보를 조회할 수 있다")
    fun `can find order details`() {

        //todo
    }

}