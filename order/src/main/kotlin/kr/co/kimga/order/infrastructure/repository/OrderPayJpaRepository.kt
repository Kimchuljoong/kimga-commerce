package kr.co.kimga.order.infrastructure.repository

import kr.co.kimga.order.domain.entity.order.OrderPay
import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import org.springframework.data.jpa.repository.JpaRepository

interface OrderPayJpaRepository: JpaRepository<OrderPay, Long> {

    fun findByOrderIdAndPayMethod(orderId: Long, payMethod: PayMethod): OrderPay?
}