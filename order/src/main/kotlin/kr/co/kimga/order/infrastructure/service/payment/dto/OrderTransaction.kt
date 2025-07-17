package kr.co.kimga.order.infrastructure.service.payment.dto

import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType

data class OrderTransaction(
    val orderId: Long,
    val provider: PaymentProvider,
    val paymentType: PaymentType,
    val transactionId: String,
    val amount: Double,
)