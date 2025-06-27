package kr.co.kimga.order.domain.exception.order

class CanNotCancelItem(message: String = "상품을 취소할 수 없습니다"): RuntimeException(message)