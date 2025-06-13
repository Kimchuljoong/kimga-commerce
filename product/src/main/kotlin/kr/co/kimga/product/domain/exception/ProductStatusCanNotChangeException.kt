package kr.co.kimga.product.domain.exception

class ProductStatusCanNotChangeException : RuntimeException("상품의 상태를 변경할 수 없습니다")