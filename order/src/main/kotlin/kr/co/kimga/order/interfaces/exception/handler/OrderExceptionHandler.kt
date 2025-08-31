package kr.co.kimga.order.interfaces.exception.handler

import io.swagger.v3.oas.annotations.Hidden
import kr.co.kimga.order.infrastructure.exception.order.CanNotCancelOrderException
import kr.co.kimga.order.infrastructure.exception.order.CanNotFindOrder
import kr.co.kimga.order.infrastructure.exception.order.TransactionFailException
import kr.co.kimga.order.interfaces.controller.order.OrderApiV1
import kr.co.kimga.order.interfaces.exception.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice(assignableTypes = [OrderApiV1::class])
class OrderExceptionHandler {

    @ExceptionHandler(CanNotFindOrder::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(e: CanNotFindOrder): ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(CanNotCancelOrderException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleCancelFail(e: CanNotCancelOrderException): ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(TransactionFailException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleTransactionFail(e: TransactionFailException): ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRuntime(e: RuntimeException): ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()
}