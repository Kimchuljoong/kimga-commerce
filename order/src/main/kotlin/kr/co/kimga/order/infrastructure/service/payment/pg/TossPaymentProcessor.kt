package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProcessResult
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.random.Random

@Component
class TossPaymentProcessor: PaymentProcessor {
    override fun isSupported(provider: PaymentProvider): Boolean =
        (provider == PaymentProvider.TOSS)

    override fun process(request: RequestPayment): PaymentResult {
        val resultCode = randomResult(request.paymentType)
        val transactionId = makeRandomNumber()
        val approvedAt = resultCode.takeIf { it == "100" || it == "200" }?.let { Instant.now() }
        return PaymentResult(result = resultCode, transactionId = transactionId, approvedAt = approvedAt, mappedResult = resultMapping(resultCode))
    }

    override fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult {
        val resultCode = randomCancelResult(request.paymentType)
        val transactionId = makeRandomNumber()
        val approvedAt = resultCode.takeIf { it == "201" }?.let { Instant.now() }
        return CancelPaymentResult(result = resultCode, transactionId = transactionId, approvedAt = approvedAt, mappedResult = resultMapping(resultCode))
    }

    override fun resultMapping(resultCode: String): PaymentProcessResult {
        return when(resultCode) {
            "100" -> PaymentProcessResult.ACCOUNT
            "200" -> PaymentProcessResult.PAID
            "201" -> PaymentProcessResult.REFUNDED
            else -> PaymentProcessResult.READY
        }
    }

    private fun randomResult(paymentType: PaymentType): String {
        val successResult = when(paymentType) {
            PaymentType.CARD -> "200"
            else -> "100"
        }
        val failResults = listOf("300", "400", "500")
        val chance = Random.nextDouble()
        if (chance < 0.001) throw RuntimeException("payment process failure occurred!")
        if (chance < 0.05) return failResults.random()
        return successResult
    }

    private fun randomCancelResult(paymentType: PaymentType): String {
        val successResult = "201"
        val failResults = listOf("301", "401", "501")
        val chance = Random.nextDouble()
        if (chance < 0.001) throw RuntimeException("payment process failure occurred!")
        if (chance < 0.05) return failResults.random()
        return successResult
    }

    private fun makeRandomNumber() = System.currentTimeMillis().toString()
}