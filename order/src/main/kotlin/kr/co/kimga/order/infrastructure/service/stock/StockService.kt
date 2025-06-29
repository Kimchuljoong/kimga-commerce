package kr.co.kimga.order.infrastructure.service

import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.exception.stock.CanNotFindStock
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.dto.stock.RequestCreateStockDto
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class StockService(
    private val stockRepository: StockJpaRepository
) {

    @Transactional
    fun createStock(requestCreateStockDto: RequestCreateStockDto) {
        val findStock = stockRepository

        val newStock = Stock(
            productId = requestCreateStockDto.productId,
            totalInventory = requestCreateStockDto.stock,
        )
        stockRepository.save(newStock)
    }

    @Transactional
    fun decreaseInventory(productId: Long, quantity: Long) {
        val findStock = stockRepository.findStockByProductId(productId)
            ?: throw CanNotFindStock()
        findStock.decreaseInventory(quantity)
    }

    @Transactional
    fun restoreInventory(productId: Long, quantity: Long) {
        val findStock = stockRepository.findStockByProductId(productId)
            ?: throw CanNotFindStock()
        findStock.restoreInventory(quantity)
    }

    fun getAvailableProductInventory(productId: Long): Long {
        val findStock = (stockRepository.findStockByProductId(productId)
            ?: throw CanNotFindStock())
        return findStock.getAvailableInventory()
    }
}