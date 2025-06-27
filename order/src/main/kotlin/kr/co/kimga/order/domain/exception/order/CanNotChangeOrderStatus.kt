package kr.co.kimga.order.domain.exception.order

class CanNotChangeOrderStatus: RuntimeException("주문의 상태를 변경할 수 없습니다")