package kr.co.kimga.order.infrastructure.service.order

import kr.co.kimga.order.domain.entity.order.Order
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.order.enums.PayStatus
import kr.co.kimga.order.infrastructure.exception.order.CanNotCancelOrderException
import kr.co.kimga.order.infrastructure.exception.order.CanNotFindOrder
import kr.co.kimga.order.infrastructure.exception.order.CanNotFoundOrderPayException
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderPayJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderQuerydslRepository
import kr.co.kimga.order.infrastructure.service.order.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderJpaRepository,
    private val orderQuerydslRepository: OrderQuerydslRepository,
    private val orderPayRepository: OrderPayJpaRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createOrder(requestCreateOrderDto: RequestCreateOrderDto): Long {
        val newOrder = Order.of(requestCreateOrderDto)
        return orderRepository.save(newOrder).id!!
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        findOrder(orderId).cancel()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateOrderPaySucceed(orderId: Long, payMethod: PayMethod) {
        findOrderPay(orderId, payMethod).paymentStatusSucceed()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateOrderPayRefund(orderId: Long, payMethod: PayMethod) {
        findOrderPay(orderId, payMethod).paymentStatusRefund()
    }

    @Transactional
    fun completeDelivery(orderId: Long) {
        findOrder(orderId).completeDelivery()
    }

    @Transactional
    fun completePaymentForOrder(orderId: Long) {
        val order = findOrder(orderId)

        if (order.orderPays.isEmpty())
            throw CanNotFoundOrderPayException()

        if (order.orderPays.all { it.status == PayStatus.SUCCEED }) {
            order.completePaid()
        }
    }

    @Transactional
    fun cancelAble(orderId: Long) {
        if (!findOrder(orderId).cancelAble()) {
            throw CanNotCancelOrderException()
        }
    }

    @Transactional(readOnly = true)
    fun findOrders(
        requestFindOrdersDto: RequestFindOrdersDto,
        pageable: Pageable
    ): Page<FindOrderDto> =
        orderQuerydslRepository.findOrders(
            requestFindOrdersDto.memberId,
            requestFindOrdersDto.orderStatus,
            requestFindOrdersDto.from,
            requestFindOrdersDto.to,
            pageable
        ).map { it.toFindOrderDto() }

    @Transactional(readOnly = true)
    fun findOrderDetails(orderId: Long): FindOrderDetailsDto {
        val order = findOrder(orderId)
        return order.toFindOrderDetailsDto()
    }

    private fun findOrder(orderId: Long): Order =
        orderRepository.findById(orderId).orElseThrow { CanNotFindOrder() }

    private fun findOrderPay(orderId: Long, payMethod: PayMethod) =
        orderPayRepository.findByOrderIdAndPayMethod(orderId, payMethod)
            ?: throw CanNotFoundOrderPayException()
}

private fun Order.toFindOrderDto(): FindOrderDto =
    FindOrderDto(
        orderId = id!!,
        memberId = memberId!!,
        orderDate = orderDate,
        orderStatus = status,
        amount = orderPays.sumOf { it.remainAmount() }
    )

private fun Order.toFindOrderDetailsDto(): FindOrderDetailsDto =
    FindOrderDetailsDto(
        orderId = id!!,
        orderStatus = status,
        orderDate = orderDate,
        items = orderItems.map {
            FindOrderItemDto(
                productId = it.productId!!,
                productName = it.productName,
                quantity = it.remainQuantity()
            )
        },
        pays = orderPays.map {
            FindOrderPaymentDto(
                id = it.id!!,
                paymentMethod = it.payMethod!!,
                paymentStatus = it.status,
                amount = it.amount
            )
        },
        payedAmount = orderPays.sumOf { it.amount },
        discountAmount = orderPays.sumOf { it.discountAmount },
        totalAmount = orderPays.sumOf { it.amount + it.discountAmount }
    )
