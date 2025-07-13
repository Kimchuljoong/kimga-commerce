package kr.co.kimga.order.interfaces.controller.stock

import kr.co.kimga.order.application.stock.StockFacade
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stock")
class StockControllerV1(
    private val stockFacade: StockFacade
) {

    @PostMapping("")
    fun applyStock(
        @RequestBody requestCreateStockDto: RequestCreateStockDto
    ) {
        stockFacade.applyStock(requestCreateStockDto)
    }

    @GetMapping("/{productId}")
    fun findStock(
        @PathVariable("productId") productId: Long
    ): Long {
        return stockFacade.findStock(productId)
    }
}