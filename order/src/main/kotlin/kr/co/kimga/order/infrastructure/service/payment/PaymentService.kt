package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.infrastructure.service.payment.dto.*
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import lombok.RequiredArgsConstructor
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PaymentService(
    private val paymentProcessors: List<PaymentProcessor>
) {
    private val processors: Map<PaymentProvider, PaymentProcessor> =
        paymentProcessors.associateBy { processor ->
            PaymentProvider.entries.firstOrNull { processor.isSupported(it) }
                ?: throw IllegalStateException("${processor::class} does not support any PaymentProvider")
        }

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

    fun savePaymentResult(requestSavePaymentResult: RequestSavePaymentResult) {

    }
}