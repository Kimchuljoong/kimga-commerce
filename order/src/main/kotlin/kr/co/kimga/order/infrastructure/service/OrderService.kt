package kr.co.kimga.order.infrastructure.service

import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.infrastructure.exception.CanNotFindOrder
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import kr.co.kimga.order.infrastructure.service.dto.OrderDto
import kr.co.kimga.order.infrastructure.service.dto.RequestMakeOrderDto
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderJpaRepository
) {

    @Transactional
    fun saveOrder(requestMakeOrderDto: RequestMakeOrderDto) {
        val newOrder = Order.of(requestMakeOrderDto)
        orderRepository.save(newOrder)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val findOrder = orderRepository.findById(orderId)
            .orElseThrow { throw CanNotFindOrder() }
        findOrder.cancel()
    }

}