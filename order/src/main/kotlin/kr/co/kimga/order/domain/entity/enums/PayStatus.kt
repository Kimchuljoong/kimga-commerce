package kr.co.kimga.order.domain.entity.enums

enum class PayStatus(var value: String) {
    SUCCEED("succeeded"),
    REFUNDED("refunded"),
}