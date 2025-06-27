package kr.co.kimga.order.domain.entity.order.enums

enum class PayStatus(var value: String) {
    SUCCEED("succeeded"),
    REFUNDED("refunded"),
}