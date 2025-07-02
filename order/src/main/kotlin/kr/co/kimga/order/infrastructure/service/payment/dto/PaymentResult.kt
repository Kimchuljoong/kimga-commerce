package kr.co.kimga.order.infrastructure.service.payment.dto

import java.time.Instant

data class PaymentResult(
    val result: String,
    val transactionId: String,
    val approvedAt: Instant? = null,
)