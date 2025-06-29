package kr.co.kimga.order.infrastructure.exception.stock

class AlreadyCreatedStock: RuntimeException("상품에 대한 재고가 이미 생성되어있습니다")