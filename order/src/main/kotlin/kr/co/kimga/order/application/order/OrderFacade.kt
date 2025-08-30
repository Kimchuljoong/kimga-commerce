package kr.co.kimga.order.application.order

import kr.co.kimga.order.domain.entity.order.enums.PayMethod
import kr.co.kimga.order.infrastructure.service.order.OrderService
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestFindOrdersDto
import kr.co.kimga.order.infrastructure.service.payment.PaymentDomainService
import kr.co.kimga.order.infrastructure.service.payment.PaymentService
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestSavePaymentResult
import kr.co.kimga.order.infrastructure.service.payment.enums.ActionType
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProcessResult
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import kr.co.kimga.order.infrastructure.service.stock.StockService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderFacade(
    private val orderService: OrderService,
    private val paymentDomainService: PaymentDomainService,
    val stockService: StockService
) {

    @Transactional
    fun createOrder(requestCreateOrderDto: RequestCreateOrderDto): Long {
        decreaseStocks(requestCreateOrderDto)
        val orderId = orderService.createOrder(requestCreateOrderDto)
        paymentDomainService.processPayments(orderId, requestCreateOrderDto.orderPays)
        orderService.completePaymentForOrder(orderId)
        return orderId
    }

    private fun decreaseStocks(requestCreateOrderDto: RequestCreateOrderDto) {
        requestCreateOrderDto.orderItems.forEach { stockService.decreaseInventory(it.productId, it.quantity) }
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        orderService.cancelAble(orderId)
        paymentDomainService.cancelPayments(orderId)
        orderService.cancelOrder(orderId)
        restoreStocks(orderId)
    }

    private fun restoreStocks(orderId: Long) {
        orderService.findOrderDetails(orderId).let { it ->
            it.items.forEach {
                stockService.restoreInventory(it.productId, it.quantity)
            }
        }
    }

    fun findOrders(
        requestFindOrdersDto: RequestFindOrdersDto,
        pageable: Pageable
    ): Page<FindOrderDto> {
        return orderService.findOrders(requestFindOrdersDto, pageable)
    }

    fun findOrderDetails(orderId: Long): FindOrderDetailsDto {
        return orderService.findOrderDetails(orderId)
    }

    fun completeDelivery(orderId: Long) {
        orderService.completeDelivery(orderId)
    }
}