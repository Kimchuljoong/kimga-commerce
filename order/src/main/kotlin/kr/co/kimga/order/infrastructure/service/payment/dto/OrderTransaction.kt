package kr.co.kimga.order.infrastructure.service.payment.dto

data class OrderTransaction(
    val orderId: Long,
    val transactionId: String,
    val amount: Double,
)