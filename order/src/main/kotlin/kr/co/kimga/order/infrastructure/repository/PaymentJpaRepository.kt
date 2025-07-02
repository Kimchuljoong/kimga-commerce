package kr.co.kimga.order.infrastructure.repository

import kr.co.kimga.order.domain.entity.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentJpaRepository: JpaRepository<Payment, Long> {
}