package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.infrastructure.service.order.OrderService
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderPayDto
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestSavePaymentResult
import kr.co.kimga.order.infrastructure.service.payment.enums.ActionType
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProcessResult
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import org.springframework.stereotype.Service

@Service
class PaymentDomainService(
    private val paymentService: PaymentService,
    private val orderService: OrderService
) {
    fun processPayments(orderId: Long, orderPays: List<RequestCreateOrderPayDto>) {
        orderPays.forEach {
            val requestPayment = RequestPayment(
                provider = PaymentProvider.valueOf(it.provider),
                paymentType = PaymentType.valueOf(it.payMethod.value),
                orderId = orderId,
                amount = it.amount
            )

            val paymentResult = paymentService.makePayment(requestPayment)

            val saveRequest = RequestSavePaymentResult(
                result = paymentResult.result,
                actionType = ActionType.PAYMENT,
                provider = requestPayment.provider,
                paymentType = requestPayment.paymentType,
                transactionId = paymentResult.transactionId,
                orderId = requestPayment.orderId,
                amount = requestPayment.amount,
                approvedAt = paymentResult.approvedAt
            )
            paymentService.savePaymentResult(saveRequest)

            if (paymentResult.mappedResult == PaymentProcessResult.PAID) {
                orderService.updateOrderPaySucceed(orderId, it.payMethod)
            }
        }
    }

    fun cancelPayments(orderId: Long) {
        paymentService.findOrderTransactions(orderId).forEach {
            val cancelRequest = RequestCancelPayment(
                orderId = it.orderId,
                provider = it.provider,
                paymentType = it.paymentType,
                amount = it.amount
            )
            val cancelResult = paymentService.cancelPayment(cancelRequest)

            val saveRequest = RequestSavePaymentResult(
                result = cancelResult.result,
                actionType = ActionType.CANCEL,
                provider = it.provider,
                paymentType = it.paymentType,
                transactionId = cancelResult.transactionId,
                orderId = it.orderId,
                amount = it.amount,
                approvedAt = cancelResult.approvedAt
            )
            paymentService.savePaymentResult(saveRequest)

            orderService.updateOrderPayRefund(orderId, PayMethod.valueOf(it.paymentType.value))
        }
    }
}
