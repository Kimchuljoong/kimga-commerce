package kr.co.kimga.order.infrastructure.service.order.dto

import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.order.enums.PayStatus

data class FindOrderPaymentDto(
    val id: Long,
    val paymentMethod: PayMethod,
    val paymentStatus: PayStatus,
    val amount: Double,
)