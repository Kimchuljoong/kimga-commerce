package kr.co.kimga.order.interfaces.controller.stock

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.springframework.web.bind.annotation.*

@Tag(name = "재고 API V1")
@RequestMapping("api/v1/stock")
interface StockApiV1 {

    @Operation(summary = "재고 적용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "재고 정상 적용"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("")
    fun applyStock(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "재고 적용 DTO",
            required = true
        )
        @RequestBody requestCreateStockDto: RequestCreateStockDto
    )

    @Operation(summary = "재고 조회")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "재고 정상 조회",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Long::class)
            )]
        ),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @GetMapping("/{productId}")
    fun findStock(
        @Parameter(
            name = "productId",
            description = "상품 ID",
            required = true,
            example = "1"
        )
        @PathVariable("productId") productId: Long
    ): Long
}
