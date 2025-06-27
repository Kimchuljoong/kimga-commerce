package kr.co.kimga.order.domain.exception.stock

class CanNotAvailableInventory(message: String = "재고를 변경할 수 없습니다"): RuntimeException(message)