package kr.co.kimga.member.infrastructure.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.kimga.member.domain.service.SessionService
import kr.co.kimga.member.infrastructure.context.MemberContext
import kr.co.kimga.member.infrastructure.context.MemberContextHolder
import kr.co.kimga.member.infrastructure.security.jwt.JwtProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    @Qualifier("accessJwtProvider")
    private val accessJwtProvider: JwtProvider,
    private val sessionService: SessionService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = extractToken(request)
        val claims = accessJwtProvider.extractClaims(token)
        val uuid = claims.subject
        val id = sessionService.findMemberId(uuid)
        MemberContextHolder.setContext(MemberContext(id!!, uuid))
        return true
    }

    private fun extractToken(request: HttpServletRequest) : String {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            throw IllegalArgumentException("Authorization header is missing or invalid")
        }

        return authHeader.removePrefix("Bearer ").trim()
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        MemberContextHolder.clear()
    }
}
