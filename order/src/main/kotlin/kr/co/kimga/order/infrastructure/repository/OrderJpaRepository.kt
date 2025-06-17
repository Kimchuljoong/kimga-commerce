package kr.co.kimga.order.infrastructure.repository

import kr.co.kimga.order.domain.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository: JpaRepository<Order, Long> {
}