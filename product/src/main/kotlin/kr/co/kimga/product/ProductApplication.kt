package kr.co.kimga.product

import config.H2PrdGuard
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(H2PrdGuard::class)
class ProductApplication

fun main(args: Array<String>) {
    runApplication<ProductApplication>(*args)
}