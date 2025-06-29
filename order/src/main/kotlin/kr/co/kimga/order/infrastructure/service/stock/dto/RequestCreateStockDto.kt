package kr.co.kimga.order.infrastructure.service.dto.stock

class RequestCreateStockDto(
    val productId: Long,
    val stock: Long = 0,
)