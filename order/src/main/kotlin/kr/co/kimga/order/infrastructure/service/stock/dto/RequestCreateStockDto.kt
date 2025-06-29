package kr.co.kimga.order.infrastructure.service.stock.dto

class RequestCreateStockDto(
    val productId: Long,
    val stock: Long = 0,
)