package kr.co.kimga.order.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.domain.entity.OrderItem
import kr.co.kimga.order.domain.entity.enums.PayMethod
import kr.co.kimga.order.domain.entity.enums.PayStatus
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderQuerydslRepository
import kr.co.kimga.order.infrastructure.service.OrderService
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderPayDto
import kr.co.kimga.order.infrastructure.service.dto.RequestCreateOrderItemDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import java.time.Instant
import kotlin.math.floor


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

        val fakeOrder = Order()

        every {
            orderJpaRepository.save(any())
        } returns fakeOrder

        // when
        orderService.createOrder(requestCreateOrderDto)

        // then
        verify { orderJpaRepository.save(any()) }
    }

    fun cancelOrder() {


    }
}