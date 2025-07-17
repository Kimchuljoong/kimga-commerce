package kr.co.kimga.order.infrastructure.service.payment.dto

sealed interface PaymentOption

data class CardOptions(
    val cardNumber: String,
) : PaymentOption