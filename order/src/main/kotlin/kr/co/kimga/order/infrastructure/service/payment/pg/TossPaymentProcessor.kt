package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider

class TossPaymentProcessor: PaymentProcessor {
    override fun isSupported(provider: PaymentProvider): Boolean =
        (provider == PaymentProvider.TOSS)

    override fun process(request: RequestPayment): PaymentResult {
        TODO("Not yet implemented")
    }

    override fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult {
        TODO("Not yet implemented")
    }
}