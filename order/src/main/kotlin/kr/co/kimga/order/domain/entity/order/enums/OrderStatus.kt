package kr.co.kimga.order.domain.entity.order.enums

enum class OrderStatus(val value: String) {
    ORDERED("Ordered"),
    PAID("Paid"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    RETURNED("Returned")
}