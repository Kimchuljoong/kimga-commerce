package kr.co.kimga.order.infrastructure.exception.stock

class CanNotFindStock: RuntimeException("상품에 대한 재고를 찾을 수 없습니다")