package kr.co.kimga.order.interfaces.controller

import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestFindOrdersDto
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @RequestBody request: RequestCreateOrderDto
    ): Long {
        return orderFacade.createOrder(request)
    }

    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable("orderId") orderId: Long)
    {
        orderFacade.cancelOrder(orderId)
    }

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

    @GetMapping("/{orderId}")
    fun findOrderDetails(
        @PathVariable("orderId") orderId: Long,
    ): FindOrderDetailsDto {
        return orderFacade.findOrderDetails(orderId)
    }

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