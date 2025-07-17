package kr.co.kimga.order.infrastructure.exception.order

class CanNotCancelOrderException: RuntimeException("주문을 취소할 수 없습니다")