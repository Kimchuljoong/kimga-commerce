package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl
import org.springframework.stereotype.Component

@Component
class ProductRoute: RouteConfig {
    override fun route(builder: RouteLocatorDsl) {
        builder.route("product-service") {
            path("/api/product/**")
            uri("http://localhost:8083")
        }
    }
}