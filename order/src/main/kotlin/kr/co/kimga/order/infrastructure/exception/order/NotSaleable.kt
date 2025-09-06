package kr.co.kimga.order.infrastructure.exception.order

class NotSaleable: RuntimeException("상품이 판매 상태가 아닙니다")