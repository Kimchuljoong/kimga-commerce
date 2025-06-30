package kr.co.kimga.order.infrastructure.service.payment

import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
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
    fun makePayment(requestPayment: RequestPayment) {
        val processor = processors[requestPayment.provider]
            ?: throw IllegalStateException("Can not use payment processor: ${requestPayment.provider}")
        processor.process(requestPayment)
    }

    @Retryable(
        value = [RuntimeException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 1.5)
    )
    fun cancelPayment(requestCancelPayment: RequestCancelPayment) {
        val processor = processors[requestCancelPayment.provider]
            ?: throw IllegalStateException("Can not use payment processor: ${requestCancelPayment.provider}")
        processor.cancelProcess(requestCancelPayment)
    }
}