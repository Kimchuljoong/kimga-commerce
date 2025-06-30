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
        return PaymentResult(result = "200")
    }

    override fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult {
        randomFail()
        return CancelPaymentResult(result = "200")
    }

    private fun randomFail() {
        val chance = Random.nextDouble()
        if (chance < 0.2) throw RuntimeException("payment process failure occurred!")
    }
}