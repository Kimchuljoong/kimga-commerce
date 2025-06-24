package kr.co.kimga.order.infrastructure.service

import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.infrastructure.exception.CanNotFindOrder
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderQuerydslRepository
import kr.co.kimga.order.infrastructure.service.dto.*
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderJpaRepository,
    private val orderQuerydslRepository: OrderQuerydslRepository
) {

    @Transactional
    fun createOrder(requestCreateOrderDto: RequestCreateOrderDto) {
        val newOrder = Order.of(requestCreateOrderDto)
        orderRepository.save(newOrder)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val findOrder = orderRepository.findById(orderId)
            .orElseThrow { throw CanNotFindOrder() }
        findOrder.cancel()
    }

    fun findOrders(
        requestFindOrdersDto: RequestFindOrdersDto,
        pageable: Pageable
    ): Page<FindOrderDto> {
        val findOrders = orderQuerydslRepository.findOrders(
            requestFindOrdersDto.memberId,
            requestFindOrdersDto.orderStatus,
            requestFindOrdersDto.from,
            requestFindOrdersDto.to,
            pageable
        )

        return findOrders.map { it ->
            FindOrderDto(
                orderId = it.id!!,
                memberId = it.memberId!!,
                orderDate = it.orderDate,
                orderStatus = it.status,
                amount = it.orderPays.sumOf { it.remainAmount() }
            )
        }
    }

    fun findOrderDetails(orderId: Long): FindOrderDetailsDto {

        val findOrder = orderRepository.findById(orderId)
            .orElseThrow { throw CanNotFindOrder() }

        return FindOrderDetailsDto(
            orderId = findOrder.id!!,
            orderStatus = findOrder.status,
            orderDate = findOrder.orderDate,
            items = findOrder.orderItems.map {
                FindOrderItemDto(
                    productId = it.productId!!,
                    productName = it.productName,
                    quantity = it.remainQuantity()
                )
            },
            payedAmount = findOrder.orderPays.sumOf { it.amount },
            discountAmount = findOrder.orderPays.sumOf { it.discountAmount },
            totalAmount = findOrder.orderPays.sumOf { it.amount + it.discountAmount }
        )
    }
}