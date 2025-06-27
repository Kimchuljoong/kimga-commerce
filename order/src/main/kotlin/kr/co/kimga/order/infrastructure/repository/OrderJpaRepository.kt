package kr.co.kimga.order.infrastructure.repository

import kr.co.kimga.order.domain.entity.order.Order
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository: JpaRepository<Order, Long> {

    fun findOrdersByStatus(status: OrderStatus): List<Order>
}