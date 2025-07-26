package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl
import org.springframework.stereotype.Component

@Component
class MemberRoute: RouteConfig {
    override fun route(builder: RouteLocatorDsl) {
        builder.route("member-service") {
            path("/api/member/**")
            uri("http://localhost:8084")
        }
    }
}