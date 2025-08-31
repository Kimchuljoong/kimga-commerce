package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.domain.entity.payment.Payment
import kr.co.kimga.order.infrastructure.repository.PaymentJpaRepository
import kr.co.kimga.order.infrastructure.service.payment.dto.*
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    paymentProcessors: List<PaymentProcessor>,
    private val paymentRepository: PaymentJpaRepository
) {

    private val processors: Map<PaymentProvider, PaymentProcessor> =
        PaymentProvider.entries.associateWith { provider ->
            paymentProcessors.firstOrNull { it.isSupported(provider) }
                ?: throw IllegalStateException("No processor found for $provider")
        }

    @Transactional
    @Retryable(
        value = [RuntimeException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 1.5)
    )
    fun makePayment(requestPayment: RequestPayment): PaymentResult =
        getProcessor(requestPayment.provider).process(requestPayment)

    @Transactional
    @Retryable(
        value = [RuntimeException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 1.5)
    )
    fun cancelPayment(requestCancelPayment: RequestCancelPayment): CancelPaymentResult =
        getProcessor(requestCancelPayment.provider).cancelProcess(requestCancelPayment)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun savePaymentResult(requestSavePaymentResult: RequestSavePaymentResult) {
        paymentRepository.save(toPaymentEntity(requestSavePaymentResult))
    }

    fun findOrderTransactions(orderId: Long): List<OrderTransaction> =
        paymentRepository.findByOrderId(orderId).map { it.toOrderTransaction() }

    private fun getProcessor(provider: PaymentProvider): PaymentProcessor =
        processors[provider]
            ?: throw IllegalStateException("Can not use payment processor: $provider")

    private fun toPaymentEntity(dto: RequestSavePaymentResult): Payment =
        Payment(
            orderId = dto.orderId,
            actionType = dto.actionType,
            provider = dto.provider,
            paymentType = dto.paymentType,
            amount = dto.amount,
            status = dto.paymentStatus,
            transactionId = dto.transactionId,
            approvedAt = dto.approvedAt
        )

    private fun Payment.toOrderTransaction(): OrderTransaction =
        OrderTransaction(
            orderId = orderId!!,
            transactionId = transactionId!!,
            provider = provider!!,
            paymentType = paymentType!!,
            amount = amount
        )
}
