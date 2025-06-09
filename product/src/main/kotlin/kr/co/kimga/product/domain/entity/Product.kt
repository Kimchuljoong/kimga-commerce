package kr.co.kimga.product.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kr.co.kimga.product.domain.ProductStatus
import java.time.Instant

@Entity
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null,

    val productName: String = "",
    val productStatus: ProductStatus = ProductStatus.SALE,
    val price: Double = 0.0,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)