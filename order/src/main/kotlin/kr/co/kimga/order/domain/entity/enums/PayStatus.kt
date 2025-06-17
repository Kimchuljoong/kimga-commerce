package kr.co.kimga.order.domain.entity.enums

enum class PayStatus(var value: String) {
    PENDING("pending"),
    PROCESSING("processing"),
    SUCCEED("succeeded"),
    FAILED("failed"),
}