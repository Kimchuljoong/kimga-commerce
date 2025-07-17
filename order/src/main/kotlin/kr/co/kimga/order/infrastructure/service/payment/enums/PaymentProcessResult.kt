package kr.co.kimga.order.infrastructure.service.payment.enums

enum class PaymentProcessResult(val value: String) {
    ACCOUNT("ACCOUNT"),
    READY("READY"),
    PAID("PAID"),
    REFUNDED("REFUNDED"),
}