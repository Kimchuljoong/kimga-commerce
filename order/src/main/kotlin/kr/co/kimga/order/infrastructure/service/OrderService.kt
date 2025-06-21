package kr.co.kimga.order.infrastructure.service

import kr.co.kimga.order.common.annotation.DistributedLock
import kr.co.kimga.order.infrastructure.repository.OrderJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderJpaRepository
) {

    @Transactional
    @DistributedLock(key = "'stock:' + #productId'")
    fun makeOrder() {

    }
}