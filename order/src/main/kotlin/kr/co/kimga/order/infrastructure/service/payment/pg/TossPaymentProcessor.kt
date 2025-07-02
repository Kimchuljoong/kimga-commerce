package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import java.time.Instant
import kotlin.random.Random

class TossPaymentProcessor: PaymentProcessor {
    override fun isSupported(provider: PaymentProvider): Boolean =
        (provider == PaymentProvider.TOSS)

    override fun process(request: RequestPayment): PaymentResult {
        val resultCode = randomResult()
        val transactionId = makeRandomNumber()
        val approvedAt = resultCode.takeIf { it == "200" }?.let { Instant.now() }
        return PaymentResult(result = resultCode, transactionId = transactionId, approvedAt = approvedAt)
    }

    override fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult {
        val resultCode = randomResult()
        val transactionId = makeRandomNumber()
        val approvedAt = resultCode.takeIf { it == "200" }?.let { Instant.now() }
        return CancelPaymentResult(result = resultCode, transactionId = transactionId, approvedAt = approvedAt)
    }

    private fun randomResult(): String {
        val successResult = "200"
        val failResults = listOf("300", "400", "500")
        val chance = Random.nextDouble()
        if (chance < 0.2) throw RuntimeException("payment process failure occurred!")
        if (chance < 0.4) return failResults.random()
        return successResult
    }

    private fun makeRandomNumber() = System.currentTimeMillis().toString()
}