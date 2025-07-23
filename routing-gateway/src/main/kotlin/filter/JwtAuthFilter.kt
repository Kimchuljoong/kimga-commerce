package kr.co.kimga.routingGateway.filter

import kr.co.kimga.routingGateway.provider.JwtProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class JwtAuthFilter(
    @Qualifier("jwtAccessProvider")
    private val jwtAccessProvider: JwtProvider
): AbstractGatewayFilterFactory<Any>(Any::class.java) {

    override fun apply(config: Any?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request
            val token = request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.removePrefix("Bearer ")

            if (token.isNullOrBlank()) {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return@GatewayFilter exchange.response.setComplete()
            }

            try {
                jwtAccessProvider.validateToken(token)
                chain.filter(exchange)
            } catch (e: Exception) {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                exchange.response.setComplete()
            }
        }
    }
}