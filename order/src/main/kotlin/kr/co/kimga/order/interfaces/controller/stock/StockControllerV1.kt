package kr.co.kimga.order.interfaces.controller.stock

import kr.co.kimga.order.application.stock.StockFacade
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.springframework.web.bind.annotation.RestController

@RestController
class StockControllerV1(
    private val stockFacade: StockFacade
) : StockApiV1 {

    override fun applyStock(requestCreateStockDto: RequestCreateStockDto) =
        stockFacade.applyStock(requestCreateStockDto)

    override fun findStock(productId: Long): Long =
        stockFacade.findStock(productId)
}
