package kr.co.kimga.order.infrastructure.service.payment.dto

import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType

data class RequestPayment(
    val provider: PaymentProvider,
    val paymentType : PaymentType,
    val orderId: Long,
    val amount: Long,
    val options: PaymentOption
)