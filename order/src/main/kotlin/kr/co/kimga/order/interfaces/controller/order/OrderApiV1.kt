package kr.co.kimga.order.interfaces.controller.order

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/orders")
@Tag(name = "주문 API V1")
interface OrderApiV1 {

    @Operation(summary = "주문 생성", description = "신규 주문을 생성한다")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "주문 생성 완료",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Long::class)
            )]
        ),
        ApiResponse(responseCode = "400", description = "상품이 판매중이 아님"),
        ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody request: RequestCreateOrderDto): Long


    @Operation(summary = "주문 취소", description = "주문을 취소한다")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "주문 취소 완료"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(
        @Parameter(description = "취소할 주문의 ID", example = "1")
        @PathVariable orderId: Long
    )


    @Operation(summary = "주문들 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "주문들 정상 조회",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Page::class)
            )]
        ),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @GetMapping
    fun findOrders(
        @RequestHeader("X-User-Id") memberId: Long,
        @RequestParam("status") status: OrderStatus?,
        @RequestParam from: String,
        @RequestParam to: String,
        @RequestParam("page", defaultValue = "0") page: Int,
    ): Page<FindOrderDto>


    @Operation(summary = "주문 상세 조회", description = "주문 상세를 조회할 수 있다")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "주문 상세 정상 조회",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FindOrderDetailsDto::class)
            )]
        ),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @GetMapping("/{orderId}")
    fun findOrderDetails(
        @Parameter(description = "조회할 주문 ID", example = "1")
        @PathVariable orderId: Long
    ): FindOrderDetailsDto


    @Operation(summary = "배송 완료 처리", description = "배송 완료 처리를 할 수 있다")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "배송 완료 정상 처리"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/{orderId}/delivery/complete")
    fun completeDelivery(
        @Parameter(description = "배송 완료 처리할 주문 ID", example = "1")
        @PathVariable orderId: Long
    )
}
