package kr.co.kimga.product.domain.entity

import jakarta.persistence.*
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import java.time.Instant

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null,

    val productName: String = "",

    @Enumerated(EnumType.STRING)
    val productStatus: ProductStatus = ProductStatus.SALE,
    val price: Double = 0.0,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)