package kr.co.kimga.order.application.stock

import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.service.stock.StockService
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockFacade(
    private val stockService: StockService
) {

    @Transactional
    fun applyStock(requestCreateStockDto: RequestCreateStockDto) {
        stockService.createStock(requestCreateStockDto)
        stockService.applyInventory(requestCreateStockDto.productId, requestCreateStockDto.stock)
    }

    fun findStock(productId: Long): Long {
        return stockService.getAvailableProductInventory(productId)
    }
}