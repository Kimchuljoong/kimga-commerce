package kr.co.kimga.order.infrastructure.service.payment.dto

import kr.co.kimga.order.domain.entity.payment.enums.PaymentStatus
import kr.co.kimga.order.infrastructure.service.payment.enums.ActionType
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import java.time.Instant

data class RequestSavePaymentResult(
    val result: String,
    val actionType: ActionType,
    val paymentType: PaymentType,
    val transactionId: String,
    val orderId: Long? = null,
    val amount: Double = 0.0,
    val approvedAt: Instant? = null,
) {
    val paymentStatus: PaymentStatus =
        when {
            actionType == ActionType.PAYMENT && result == "200" -> PaymentStatus.SUCCEED
            actionType == ActionType.CANCEL && result == "200" -> PaymentStatus.CANCELED
            result in listOf("300", "400", "500") -> PaymentStatus.FAIL
            else -> PaymentStatus.READY
        }

}