package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.infrastructure.exception.order.TransactionFailException
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
import java.time.Instant

@Service
class PaymentDomainService(
    private val paymentService: PaymentService,
    private val orderService: OrderService
) {
    fun processPayments(orderId: Long, orderPays: List<RequestCreateOrderPayDto>) {
        val successPayments = mutableListOf<RequestCreateOrderPayDto>()

        try {
            orderPays.forEach { payDto ->
                val paymentResult = makePayment(orderId, payDto)

                if (paymentResult.mappedResult == PaymentProcessResult.PAID) {
                    orderService.updateOrderPaySucceed(orderId, payDto.payMethod)
                    successPayments.add(payDto)
                } else {
                    throw TransactionFailException("${payDto.payMethod} 결제를 실패했습니다")
                }
            }
        } catch (ex: Exception) {
            rollbackPayments(orderId, successPayments)
            throw ex
        }
    }

    private fun makePayment(orderId: Long, payDto: RequestCreateOrderPayDto) =
        RequestPayment(
            provider = PaymentProvider.valueOf(payDto.provider),
            paymentType = PaymentType.valueOf(payDto.payMethod.value),
            orderId = orderId,
            amount = payDto.amount
        ).let { request ->
            val result = paymentService.makePayment(request)

            savePaymentResult(
                result.result,
                ActionType.PAYMENT,
                request.provider,
                request.paymentType,
                result.transactionId,
                request.orderId,
                request.amount,
                result.approvedAt
            )
            result
        }

    private fun rollbackPayments(orderId: Long, successPayments: List<RequestCreateOrderPayDto>) {
        successPayments.asReversed().forEach { payDto ->
            val paymentType = PaymentType.valueOf(payDto.payMethod.value)
            val provider = PaymentProvider.valueOf(payDto.provider)

            val cancelResult = paymentService.cancelPayment(
                RequestCancelPayment(
                    orderId = orderId,
                    provider = provider,
                    paymentType = paymentType,
                    amount = payDto.amount
                )
            )

            savePaymentResult(
                cancelResult.result,
                ActionType.CANCEL,
                provider,
                paymentType,
                cancelResult.transactionId,
                orderId,
                payDto.amount,
                cancelResult.approvedAt
            )

            orderService.updateOrderPayRefund(orderId, payDto.payMethod)
        }
    }

    private fun savePaymentResult(
        result: String,
        actionType: ActionType,
        provider: PaymentProvider,
        paymentType: PaymentType,
        transactionId: String,
        orderId: Long,
        amount: Double,
        approvedAt: Instant?
    ) {
        paymentService.savePaymentResult(
            RequestSavePaymentResult(
                result = result,
                actionType = actionType,
                provider = provider,
                paymentType = paymentType,
                transactionId = transactionId,
                orderId = orderId,
                amount = amount,
                approvedAt = approvedAt
            )
        )
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
