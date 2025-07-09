package kr.co.kimga.order.interfaces.controller

import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
class OrderControllerV1(
    private val orderFacade: OrderFacade
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody request: RequestCreateOrderDto): Long {
        return orderFacade.createOrder(request)
    }

    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable("orderId") orderId: Long) {
        orderFacade.cancelOrder(orderId)
    }

    @GetMapping
    fun findOrders(
        @RequestHeader("X-User-Id") memberId: Long,
        @RequestParam("status") status: OrderStatus?,
        @RequestParam from: Instant,
        @RequestParam to: Instant,
        @RequestParam("page", defaultValue = "0") page: Int,
    ): Page<FindOrderDto> {
        val requestFindOrdersDto = RequestFindOrdersDto(memberId, status, from, to)
        val pageable = PageRequest.of(page, 10)
        return orderFacade.findOrders(requestFindOrdersDto, pageable)
    }

}