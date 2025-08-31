package kr.co.kimga.order.interfaces.exception.handler

import io.swagger.v3.oas.annotations.Hidden
import kr.co.kimga.order.domain.exception.stock.CanNotAvailableInventory
import kr.co.kimga.order.infrastructure.exception.stock.CanNotFindStock
import kr.co.kimga.order.infrastructure.exception.stock.CanNotRestoreStock
import kr.co.kimga.order.interfaces.controller.stock.StockApiV1
import kr.co.kimga.order.interfaces.exception.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice(assignableTypes = [StockApiV1::class])
class StockExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgument(e: IllegalArgumentException): ExceptionResponse =
        e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(CanNotAvailableInventory::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleCanNotAvailableInventory(e: CanNotAvailableInventory): ExceptionResponse =
        e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(CanNotRestoreStock::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleCanNotRestoreStock(e: CanNotRestoreStock): ExceptionResponse =
        e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(CanNotFindStock::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleCanNotFindStock(e: CanNotFindStock): ExceptionResponse =
        e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRuntime(e: RuntimeException): ExceptionResponse =
        e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()
}
