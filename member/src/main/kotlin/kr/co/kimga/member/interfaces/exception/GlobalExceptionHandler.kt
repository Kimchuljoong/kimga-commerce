package kr.co.kimga.member.interfaces.exception

import kr.co.kimga.member.domain.exception.CanNotRefreshTokenException
import kr.co.kimga.member.domain.exception.CanNotRenewTokenException
import kr.co.kimga.member.domain.exception.MemberDuplicatedException
import kr.co.kimga.member.domain.exception.MemberNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun globalException(e: RuntimeException) : ExceptionResponse {
        e.printStackTrace()
        return e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()
    }

    @ExceptionHandler(MemberNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun memberNotFoundException(e: MemberNotFoundException) : ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(MemberDuplicatedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun memberDuplicateException(e: MemberDuplicatedException) : ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

    @ExceptionHandler(CanNotRefreshTokenException::class, CanNotRenewTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun tokenException(e: RuntimeException) : ExceptionResponse = e.message?.let { ExceptionResponse(it) } ?: ExceptionResponse()

}