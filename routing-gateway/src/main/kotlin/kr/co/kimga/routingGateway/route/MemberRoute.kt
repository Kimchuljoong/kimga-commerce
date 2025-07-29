package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder
import org.springframework.stereotype.Component

@Component
class MemberRoute: RouteConfig {
    override fun route(builder: Builder) {
        builder.route("member-service") {
            it.path("/api/member/**")
                .uri("http://localhost:8084")
        }
    }
}