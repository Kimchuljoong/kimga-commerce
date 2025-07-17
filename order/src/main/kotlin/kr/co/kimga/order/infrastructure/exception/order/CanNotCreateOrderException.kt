package kr.co.kimga.order.infrastructure.exception.order

class CanNotCreateOrderException(message: String = "주문은 생성할 수 없습니다"): RuntimeException(message)