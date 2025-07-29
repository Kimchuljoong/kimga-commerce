package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder
import org.springframework.stereotype.Component

@Component
class ProductRoute: RouteConfig {
    override fun route(builder: Builder) {
        builder.route("product-service") {
            it.path("/api/product/**")
                .uri("http://localhost:8083")
        }
    }
}