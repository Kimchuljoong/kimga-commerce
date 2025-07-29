package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RouteApplyConfig(
    private val routeConfigs: List<RouteConfig>
) {

    @Bean
    fun routeConfig(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes().apply {
            routeConfigs.forEach { r -> r.route(this) }
        }.build()
}