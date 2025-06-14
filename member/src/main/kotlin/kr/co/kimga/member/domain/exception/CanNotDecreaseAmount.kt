package kr.co.kimga.member.domain.exception

class CanNotDecreaseAmount : RuntimeException("잔액보다 차감액이 더 큽니다")
