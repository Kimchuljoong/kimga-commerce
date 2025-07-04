package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.domain.entity.order.OrderItem
import kr.co.kimga.order.domain.entity.payment.Payment
import kr.co.kimga.order.infrastructure.repository.PaymentJpaRepository
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderItemDto
import kr.co.kimga.order.infrastructure.service.payment.dto.*
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import lombok.RequiredArgsConstructor
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.toList

@Service
@RequiredArgsConstructor
class PaymentService(
    paymentProcessors: List<PaymentProcessor>,
    private val paymentRepository: PaymentJpaRepository
) {
    private val processors: Map<PaymentProvider, PaymentProcessor> =
        paymentProcessors.associateBy { processor ->
            PaymentProvider.entries.firstOrNull { processor.isSupported(it) }
                ?: throw IllegalStateException("${processor::class} does not support any PaymentProvider")
        }

    @Transactional
    @Retryable(
        value = [RuntimeException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 1.5)
    )
    fun makePayment(requestPayment: RequestPayment): PaymentResult {
        val processor = processors[requestPayment.provider]
            ?: throw IllegalStateException("Can not use payment processor: ${requestPayment.provider}")
        return processor.process(requestPayment)
    }

    @Transactional
    @Retryable(
        value = [RuntimeException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 1.5)
    )
    fun cancelPayment(requestCancelPayment: RequestCancelPayment): CancelPaymentResult {
        val processor = processors[requestCancelPayment.provider]
            ?: throw IllegalStateException("Can not use payment processor: ${requestCancelPayment.provider}")
        return processor.cancelProcess(requestCancelPayment)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun savePaymentResult(requestSavePaymentResult: RequestSavePaymentResult) {
        val newPayment = Payment(
            orderId = requestSavePaymentResult.orderId,
            actionType = requestSavePaymentResult.actionType,
            provider = requestSavePaymentResult.provider,
            paymentType = requestSavePaymentResult.paymentType,
            amount = requestSavePaymentResult.amount,
            status = requestSavePaymentResult.paymentStatus,
            transactionId = requestSavePaymentResult.transactionId,
            approvedAt = requestSavePaymentResult.approvedAt,
        )
        paymentRepository.save(newPayment)
    }

    fun findOrderTransactions(orderId: Long): List<OrderTransaction> {
        val findPayments = paymentRepository.findByOrderId(orderId)
        return findPayments.map {
            OrderTransaction(
                orderId = it.orderId!!,
                transactionId =  it.transactionId!!,
                provider = it.provider!!,
                paymentType = it.paymentType!!,
                amount = it.amount)
        }.toList()
    }

}