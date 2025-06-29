package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import org.springframework.stereotype.Service

@Service
class PaymentService(
    List<PaymentProcessor> paymentProcessor
) {

}