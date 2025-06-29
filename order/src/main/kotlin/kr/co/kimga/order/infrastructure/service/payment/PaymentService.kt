package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PaymentService(
    private val paymentProcessor: List<PaymentProcessor>
) {

}