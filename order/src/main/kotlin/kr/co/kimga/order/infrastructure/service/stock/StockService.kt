package kr.co.kimga.order.infrastructure.service.stock

import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.exception.stock.CanNotFindStock
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
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
        stockRepository.findStockByProductId(requestCreateStockDto.productId)
            ?: run {
                val newStock = Stock(
                    productId = requestCreateStockDto.productId,
                )
                stockRepository.save(newStock)
            }
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

    @Transactional
    fun applyInventory(productId: Long, quantity: Long) {
        val findStock = stockRepository.findStockByProductId(productId)
            ?: throw CanNotFindStock()
        findStock.applyInventory(quantity)
    }

    fun getAvailableProductInventory(productId: Long): Long {
        val findStock = (stockRepository.findStockByProductId(productId)
            ?: throw CanNotFindStock())
        return findStock.getAvailableInventory()
    }
}