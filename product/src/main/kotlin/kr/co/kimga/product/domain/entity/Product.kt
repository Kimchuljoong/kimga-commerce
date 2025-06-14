package kr.co.kimga.product.domain.entity

import jakarta.persistence.*
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.exception.PriceCanNotChangeException
import kr.co.kimga.product.domain.exception.ProductStatusCanNotChangeException
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null,

    var productName: String = "",

    @Enumerated(EnumType.STRING)
    var productStatus: ProductStatus = ProductStatus.SALE,
    var price: Double = 0.0,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
) {
    fun sale() {
        requireStatus(ProductStatus.WAIT)
        productStatus = ProductStatus.SALE
    }

    fun close() {
        requireStatus(ProductStatus.SALE, ProductStatus.WAIT)
        productStatus = ProductStatus.CLOSE
    }

    fun changePrice(newPrice: Double) {
        require(newPrice >= 0) { throw PriceCanNotChangeException() }
        price = newPrice
    }

    fun changeProductName(newName: String) {
        if (productName.isNotBlank())
            productName = newName
    }

    private fun requireStatus(vararg allowed: ProductStatus) {
        if (productStatus !in allowed) {
            throw ProductStatusCanNotChangeException()
        }
    }
}