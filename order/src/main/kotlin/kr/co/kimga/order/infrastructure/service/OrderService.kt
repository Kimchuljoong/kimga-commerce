package kr.co.kimga.order.infrastructure.service

import com.querydsl.core.QueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.domain.entity.QOrder
import kr.co.kimga.order.infrastructure.exception.CanNotFindOrder
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.repository.OrderQuerydslRepository
import kr.co.kimga.order.infrastructure.service.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.dto.RequestFindOrdersDto
import kr.co.kimga.order.infrastructure.service.dto.RequestMakeOrderDto
import lombok.RequiredArgsConstructor
import org.hibernate.annotations.processing.Find
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
    fun createOrder(requestMakeOrderDto: RequestMakeOrderDto) {
        val newOrder = Order.of(requestMakeOrderDto)
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

        return findOrders.map {
            FindOrderDto(
                orderId = it.id!!,
                memberId = it.memberId!!,
                orderDate = it.orderDate,
                orderStatus = it.status,
                amount = it.orderPays.sumOf { it.remainAmount() }
            )
        }
    }

    fun findOrderDetails(orderId: Long): FindOrderDto? {

        val findOrder = orderRepository.findById(orderId)
            .orElseThrow { throw CanNotFindOrder() }

        return null
    }
}