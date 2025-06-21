package kr.co.kimga.order.domain.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val memberId: Long? = null,

    val orderDate: Instant = Instant.now(),

    @OneToMany(mappedBy= "order",  fetch = FetchType.LAZY)
    val orderPays: MutableList<OrderPay> = mutableListOf(),

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    val items: MutableList<OrderItem> = mutableListOf(),

    @CreatedDate
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    val modifiedAt: Instant = Instant.now()
)