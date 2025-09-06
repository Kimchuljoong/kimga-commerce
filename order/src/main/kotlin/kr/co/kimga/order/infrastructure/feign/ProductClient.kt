package kr.co.kimga.order.infrastructure.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "product-client",
    url = "\${product-service.url}",
    path = "/api/v1/product"
)
interface ProductClient {

    @GetMapping("/status/{productId}")
    fun getProductStatus(@PathVariable productId: Long): ProductStatus
}

enum class ProductStatus(val value: String) {
    WAIT("wait"),
    SALE("sale"),
    CLOSE("close"),
}