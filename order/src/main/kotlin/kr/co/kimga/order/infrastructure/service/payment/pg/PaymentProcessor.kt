package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType

interface PaymentProcessor {
    fun isSupported(paymentType: PaymentType): Boolean
    fun process(request: RequestPayment): PaymentResult
    fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult
}