package kr.co.kimga.order.infrastructure.service.stock

import kr.co.kimga.order.common.annotation.DistributedLock
import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.exception.stock.CanNotFindStock
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
    private val stockRepository: StockJpaRepository
) {

    @Transactional
    fun createStock(request: RequestCreateStockDto) {
        stockRepository.findStockByProductId(request.productId) ?: run {
            val newStock = Stock(productId = request.productId)
            stockRepository.save(newStock)
        }
    }

    @DistributedLock(key = "'stock:' + #productId")
    @Transactional
    fun decrease(productId: Long, quantity: Long) {
        getStock(productId).decreaseInventory(quantity)
    }

    @DistributedLock(key = "'stock:' + #productId")
    @Transactional
    fun restore(productId: Long, quantity: Long) {
        getStock(productId).restoreInventory(quantity)
    }

    @DistributedLock(key = "'stock:' + #productId")
    @Transactional
    fun apply(productId: Long, quantity: Long) {
        getStock(productId).applyInventory(quantity)
    }

    @Transactional(readOnly = true)
    fun getAvailableProductInventory(productId: Long): Long =
        getStock(productId).getAvailableInventory()

    private fun getStock(productId: Long): Stock =
        stockRepository.findStockByProductId(productId) ?: throw CanNotFindStock()
}
