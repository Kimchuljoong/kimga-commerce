package kr.co.kimga.order.domain.exception.order

class CanNotRefund(message: String = "환불이 불가능 합니다"): RuntimeException(message)