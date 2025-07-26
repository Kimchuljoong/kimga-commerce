package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl
import org.springframework.stereotype.Component

@Component
class OrderRoute: RouteConfig {
    override fun route(builder: RouteLocatorDsl) {
        builder.route("order-service") {
            path("/orders/**")
            uri("http://localhost:8082")
        }
    }
}