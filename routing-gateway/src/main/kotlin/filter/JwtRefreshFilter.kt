package kr.co.kimga.routingGateway.filter

import kr.co.kimga.routingGateway.config.AuthHeaderConstants
import kr.co.kimga.routingGateway.provider.JwtProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(2)
class JwtRefreshFilter(
    @Qualifier("jwtRefreshProvider")
    private val jwtRefreshProvider: JwtProvider
): GlobalFilter {

    private val allowPathLists = listOf("/token/refresh")

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.toString()
        return when {
            allowPathLists.any{ path.startsWith(it) } -> authenticate(exchange, chain)
            else -> chain.filter(exchange)
        }
    }

    private fun authenticate(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val token = extractToken(exchange.request)
            ?.takeIf { it.isNotBlank() }
            ?: return unauthorized(exchange)

        return if (jwtRefreshProvider.validateToken(token)) {
            chain.filter(exchange)
        } else {
            unauthorized(exchange)
        }
    }

    private fun extractToken(request: ServerHttpRequest) = request.headers.getFirst(AuthHeaderConstants.X_REFRESH_TOKEN)?.trim()

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> =
        exchange.response.apply {
            statusCode = HttpStatus.UNAUTHORIZED
        }.setComplete()
}