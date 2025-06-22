package kr.co.kimga.order.domain.entity.enums

enum class OrderStatus(val value: String) {
    PROCESSING("Processing"),
    PAID("Paid"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    RETURNED("Returned")
}