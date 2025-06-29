package kr.co.kimga.order.infrastructure.service.dto.order

import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.domain.entity.order.enums.PayStatus

data class RequestCreateOrderPayDto(
    val payMethod: PayMethod,
    val discountAmount: Double,
    val amount: Double,
    val vat: Double,
    val status: PayStatus,
)