package kr.co.kimga.order.application.order

import kr.co.kimga.order.infrastructure.service.OrderService
import kr.co.kimga.order.infrastructure.service.StockService
import kr.co.kimga.order.infrastructure.service.dto.order.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.dto.order.FindOrderDto
import kr.co.kimga.order.infrastructure.service.dto.order.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.dto.order.RequestFindOrdersDto
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderFacade(
    private val orderService: OrderService,
    private val stockService: StockService
) {

    @Transactional
    fun createOrder(requestCreateOrderDto: RequestCreateOrderDto) {
        requestCreateOrderDto.orderItems.forEach{
            stockService.decreaseInventory(it.productId, it.quantity)
        }
        // todo 결제
        orderService.createOrder(requestCreateOrderDto)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val findOrderDetails = orderService.findOrderDetails(orderId)
        orderService.cancelOrder(orderId)
        // todo 결제 취소
        findOrderDetails.items.forEach {
            stockService.restoreInventory(it.productId, it.quantity)
        }
    }

    fun findOrders(
        requestFindOrdersDto: RequestFindOrdersDto,
        pageable: Pageable
    ): Page<FindOrderDto> {
        return orderService.findOrders(requestFindOrdersDto, pageable)
    }

    fun findOrderDetails(orderId: Long): FindOrderDetailsDto {
        return  orderService.findOrderDetails(orderId)
    }
}