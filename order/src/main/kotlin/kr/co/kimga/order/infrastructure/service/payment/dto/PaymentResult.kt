package kr.co.kimga.order.infrastructure.service.payment.dto

import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProcessResult
import java.time.Instant

data class PaymentResult(
    val result: String,
    val transactionId: String,
    val approvedAt: Instant? = null,
    val mappedResult: PaymentProcessResult
)