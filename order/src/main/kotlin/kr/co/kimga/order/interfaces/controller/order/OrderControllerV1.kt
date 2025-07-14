package kr.co.kimga.order.interfaces.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestFindOrdersDto
import lombok.RequiredArgsConstructor
import org.apache.kafka.shaded.com.google.protobuf.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
class OrderControllerV1(
    private val orderFacade: OrderFacade
) {

    @Operation(summary = "주문 생성", description = "신규 주문을 생성한다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "주문 생성 완료", content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Long::class)
            )]),
            ApiResponse(responseCode = "500", description = "내부 서버 오류")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @RequestBody request: RequestCreateOrderDto
    ): Long {
        return orderFacade.createOrder(request)
    }

    @Operation(summary = "주문 취소", description = "주문을 취소한다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "주문 취소 완료"),
            ApiResponse(responseCode = "500", description = "내부 서버 오류")
        ]
    )
    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable("orderId") orderId: Long)
    {
        orderFacade.cancelOrder(orderId)
    }

    @Operation(summary = "주문들 조회")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "주문들 정상 조회", content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Page::class)
            )]),
            ApiResponse(responseCode = "500", description = "내부 서버 오류")
        ]
    )
    @GetMapping
    fun findOrders(
        @RequestHeader("X-User-Id") memberId: Long,
        @RequestParam("status") status: OrderStatus?,
        @RequestParam from: String,
        @RequestParam to: String,
        @RequestParam("page", defaultValue = "0") page: Int,
    ): Page<FindOrderDto> {
        val fromDate = toInstant(from)
        val toDate = toInstant(to)
        val requestFindOrdersDto = RequestFindOrdersDto(memberId, status, fromDate, toDate)
        val pageable = PageRequest.of(page, 50)
        return orderFacade.findOrders(requestFindOrdersDto, pageable)
    }

    @Operation(summary = "주문 상세 조회", description = "주문 상세를 조회할 수 있다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "주문 상세 정상 조회", content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FindOrderDetailsDto::class)
            )]),
            ApiResponse(responseCode = "500", description = "내부 서버 오류")
        ]
    )
    @GetMapping("/{orderId}")
    fun findOrderDetails(
        @PathVariable("orderId") orderId: Long,
    ): FindOrderDetailsDto {
        return orderFacade.findOrderDetails(orderId)
    }

    @Operation(summary = "배송 완료 처리", description = "배송 완료 처리를 할 수 있다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "배송 완료 정상 처리"),
            ApiResponse(responseCode = "500", description = "내부 서버 오류")
        ]
    )
    @PostMapping("/{orderId}/delivery/complete")
    fun completeDelivery(
        @PathVariable orderId: Long
    ) {
        orderFacade.completeDelivery(orderId)
    }

    fun toInstant(str: String): Instant{
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val localDateTime = LocalDateTime.parse(str, formatter)
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant()
    }

}