package kr.co.kimga.order.infrastructure.service.payment.dto

import java.time.Instant

data class CancelPaymentResult(
    val result: String,
    val transactionId: String,
    val approvedAt: Instant? = null,
)