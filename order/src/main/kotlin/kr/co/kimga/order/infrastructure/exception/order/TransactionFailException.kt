package kr.co.kimga.order.infrastructure.exception.order

class TransactionFailException(message: String = "결제가 실패했습니다"): RuntimeException(message)