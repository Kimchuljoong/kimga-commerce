package kr.co.kimga.order.interfaces.controller.order

import kr.co.kimga.order.application.order.OrderFacade
import kr.co.kimga.order.domain.entity.order.enums.OrderStatus
import kr.co.kimga.order.infrastructure.service.order.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RestController
class OrderControllerV1(
    private val orderFacade: OrderFacade
) : OrderApiV1 {

    override fun createOrder(request: RequestCreateOrderDto): Long {
        return orderFacade.createOrder(request)
    }

    override fun cancelOrder(orderId: Long) {
        orderFacade.cancelOrder(orderId)
    }

    override fun findOrders(
        memberId: Long,
        status: OrderStatus?,
        from: String,
        to: String,
        page: Int
    ): Page<FindOrderDto> {
        val fromDate = toInstant(from)
        val toDate = toInstant(to)
        val requestFindOrdersDto = RequestFindOrdersDto(memberId, status, fromDate, toDate)
        val pageable = PageRequest.of(page, 50)
        return orderFacade.findOrders(requestFindOrdersDto, pageable)
    }

    override fun findOrderDetails(orderId: Long): FindOrderDetailsDto {
        return orderFacade.findOrderDetails(orderId)
    }

    override fun completeDelivery(orderId: Long) {
        orderFacade.completeDelivery(orderId)
    }

    private fun toInstant(str: String): Instant {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val localDateTime = LocalDateTime.parse(str, formatter)
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant()
    }
}
