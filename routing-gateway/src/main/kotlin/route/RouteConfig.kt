package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl

interface RouteConfig {
    fun route(builder: RouteLocatorDsl)
}