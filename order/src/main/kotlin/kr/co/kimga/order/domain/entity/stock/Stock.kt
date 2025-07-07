package kr.co.kimga.order.domain.entity.stock

import jakarta.persistence.*
import kr.co.kimga.order.domain.exception.stock.CanNotAvailableInventory
import kr.co.kimga.order.infrastructure.exception.stock.CanNotRestoreStock
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "stock")
@EntityListeners(AuditingEntityListener::class)
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val productId: Long? = null,

    var totalInventory: Long = 0L,
    var orderedInventory: Long = 0L,

    @CreatedDate
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    var modifiedAt: Instant = Instant.now()
) {

    fun getAvailableInventory(): Long {
        return totalInventory - orderedInventory
    }

    fun decreaseInventory(quantity: Long) {
        if (getAvailableInventory() - quantity < 0)
            throw CanNotAvailableInventory()
        orderedInventory += quantity
    }

    fun restoreInventory(quantity: Long) {
        if (orderedInventory - quantity < 0)
            throw CanNotRestoreStock()
        orderedInventory -= quantity
    }

    fun applyInventory(quantity: Long) {
        totalInventory += quantity
    }
}