package kr.co.kimga.order.domain.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "orderitems")
data class OrderItem (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order? = null,

    @Column(nullable = false)
    val productId: Long? = null,

    val productName: String = "",
    val price: Double = 0.0,
    val vat: Double = 0.0,

    val quantity: Long = 0,

    @CreatedDate
    val createdAt: Instant? = null,

    @LastModifiedDate
    val modifiedAt: Instant? = null
)