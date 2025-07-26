package kr.co.kimga.routingGateway.filter

import kr.co.kimga.routingGateway.provider.JwtProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(1)
class JwtAuthFilter(
    @Qualifier("jwtAccessProvider")
    private val jwtAccessProvider: JwtProvider
): GlobalFilter {

    private val whiteListPaths = emptyList<String>()

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.toString()

        return when {
            isWhitelisted(path) -> chain.filter(exchange)
            else -> authenticate(exchange, chain)
        }
    }

    private fun isWhitelisted(path: String) = whiteListPaths.any { path.startsWith(it) }

    private fun authenticate(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val token = extractToken(exchange.request)
            ?.takeIf { it.isNotBlank() }
            ?: return unauthorized(exchange)

        return if (jwtAccessProvider.validateToken(token)) {
            chain.filter(exchange)
        } else {
            unauthorized(exchange)
        }
    }

    private fun extractToken(request: ServerHttpRequest) = request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.removePrefix("Bearer ")?.trim()

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> =
        exchange.response.apply {
            statusCode = HttpStatus.UNAUTHORIZED
        }.setComplete()
}