package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder
import org.springframework.stereotype.Component

@Component
class OrderRoute: RouteConfig {
    override fun route(builder: Builder) {
        builder.route("order-service") {
            it.path("/api/orders/**")
                .uri("http://localhost:8082")
        }
    }
}