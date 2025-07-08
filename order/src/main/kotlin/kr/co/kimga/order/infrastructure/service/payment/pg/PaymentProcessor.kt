package kr.co.kimga.order.infrastructure.service.payment.pg

import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProcessResult
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import org.springframework.stereotype.Component

interface PaymentProcessor {
    fun isSupported(provider: PaymentProvider): Boolean
    fun process(request: RequestPayment): PaymentResult
    fun cancelProcess(request: RequestCancelPayment): CancelPaymentResult
    fun resultMapping(resultCode: String): PaymentProcessResult
}