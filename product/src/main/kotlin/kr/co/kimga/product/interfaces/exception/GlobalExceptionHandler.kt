package kr.co.kimga.product.interfaces.exception

import kr.co.kimga.product.domain.exception.PriceCanNotChangeException
import kr.co.kimga.product.domain.exception.ProductNotFoundException
import kr.co.kimga.product.domain.exception.ProductStatusCanNotChangeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun globalException(e: RuntimeException) : ExceptionResponse {
        return e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()
    }

    @ExceptionHandler(PriceCanNotChangeException::class, ProductStatusCanNotChangeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun canNotChangeException(e: RuntimeException) : ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun productNotFoundException(e: ProductNotFoundException) : ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()
}