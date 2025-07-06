package kr.co.kimga.order.application.order

import kr.co.kimga.order.infrastructure.service.order.OrderService
import kr.co.kimga.order.infrastructure.service.stock.StockService
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDetailsDto
import kr.co.kimga.order.infrastructure.service.order.dto.FindOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestCreateOrderDto
import kr.co.kimga.order.infrastructure.service.order.dto.RequestFindOrdersDto
import kr.co.kimga.order.infrastructure.service.payment.PaymentService
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestSavePaymentResult
import kr.co.kimga.order.infrastructure.service.payment.enums.ActionType
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentProvider
import kr.co.kimga.order.infrastructure.service.payment.enums.PaymentType
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class OrderFacade(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val stockService: StockService
) {

    @Transactional
    fun createOrder(requestCreateOrderDto: RequestCreateOrderDto): Long {
        requestCreateOrderDto.orderItems.forEach{
            stockService.decreaseInventory(it.productId, it.quantity)
        }

        val orderId = orderService.createOrder(requestCreateOrderDto)

        requestCreateOrderDto.orderPays.forEach {
            val requestPayment = RequestPayment(
                provider = PaymentProvider.valueOf(it.provider),
                paymentType = PaymentType.valueOf(it.payMethod.value),
                orderId = orderId,
                amount = it.amount
            )
            val paymentResult = paymentService.makePayment(requestPayment)
            val requestSavePaymentResult = RequestSavePaymentResult(
                result = paymentResult.result,
                actionType = ActionType.PAYMENT,
                provider = PaymentProvider.valueOf(it.provider),
                paymentType = requestPayment.paymentType,
                transactionId = paymentResult.transactionId,
                orderId = requestPayment.orderId,
                amount = requestPayment.amount,
                approvedAt = paymentResult.approvedAt
            )
            paymentService.savePaymentResult(requestSavePaymentResult)
        }

        return orderId
    }

    @Transactional
    fun cancelOrder(orderId: Long) {

        paymentService.findOrderTransactions(orderId).forEach {
            val requestCancelPayment = RequestCancelPayment(
                orderId = it.orderId,
                provider = it.provider,
                paymentType = it.paymentType,
                amount = it.amount
            )
            paymentService.cancelPayment(requestCancelPayment)
        }

        orderService.findOrderDetails(orderId).let { it ->
            orderService.cancelOrder(it.orderId)
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
        return  orderService.findOrderDetails(orderId)
    }
}