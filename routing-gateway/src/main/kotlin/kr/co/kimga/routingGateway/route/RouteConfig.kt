package kr.co.kimga.routingGateway.route

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder

interface RouteConfig {
    fun route(builder: Builder)
}