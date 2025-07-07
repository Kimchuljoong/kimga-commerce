package kr.co.kimga.order.infrastructure.service.payment.enums

enum class PaymentProcessResult(val value: String) {
    READY("READY"),
    PAID("PAID"),
    REFUNDED("REFUNDED"),
}