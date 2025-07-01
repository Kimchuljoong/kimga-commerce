package kr.co.kimga.order.infrastructure.service.payment.dto

import java.time.Instant

data class RequestSavePaymentResult(
    val result: String,
    val transactionId: String,
    val orderId: Long? = null,
    val amount: Long = 0,
    val approvedAt: Instant? = null,
) {
}