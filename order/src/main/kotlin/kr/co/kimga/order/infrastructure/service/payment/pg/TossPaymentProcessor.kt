package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kotlin.random.Random

class TossPaymentProcessor: PaymentProcessor {
    override fun isSupported(provider: PaymentProvider): Boolean =
        (provider == PaymentProvider.TOSS)

    override fun process(request: RequestPayment): PaymentResult {
        randomFail()
        val transactionId = makeRandomNumber()
        return PaymentResult(result = "200", transactionId = transactionId)
    }

    override fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult {
        randomFail()
        val transactionId = makeRandomNumber()
        return CancelPaymentResult(result = "200", transactionId = transactionId)
    }

    private fun randomFail() {
        val chance = Random.nextDouble()
        if (chance < 0.2) throw RuntimeException("payment process failure occurred!")
    }

    private fun makeRandomNumber() = System.currentTimeMillis().toString()
}