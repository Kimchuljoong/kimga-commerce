package kr.co.kimga.order.infrastructure.repository

import kr.co.kimga.order.domain.entity.stock.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockJpaRepository: JpaRepository<Stock, Long> {

    fun findStockByProductId(productId: Long): Stock?
}