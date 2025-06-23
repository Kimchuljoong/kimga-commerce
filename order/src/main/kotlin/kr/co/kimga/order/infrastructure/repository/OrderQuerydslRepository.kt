package kr.co.kimga.order.infrastructure.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.kimga.order.domain.entity.Order
import kr.co.kimga.order.domain.entity.QOrder
import kr.co.kimga.order.domain.entity.enums.OrderStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class OrderQuerydslRepository(
    entityManager: EntityManager
) {

    private val queryFactory = JPAQueryFactory(entityManager)

    fun findOrders(
        memberId: Long,
        status: OrderStatus?,
        from: Instant,
        to: Instant,
        pageable: Pageable
    ): Page<Order> {

        val order = QOrder.order
        val conditions = mutableListOf<BooleanExpression>()

        memberId.let {
            conditions.add(order.memberId.eq(memberId))
        }

        status?.let {
            conditions.add(order.status.eq(it))
        }

        from.let {
            conditions.add(order.orderDate.goe(from))
        }

        to.let {
            conditions.add(order.orderDate.loe(to))
        }

        val contents = queryFactory
            .selectFrom(order)
            .where(*conditions.toTypedArray())
            .orderBy(order.orderDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(order.count())
            .from(order)
            .where(*conditions.toTypedArray())
            .fetchOne() ?: 0L

        return PageImpl(contents, pageable, total)
    }
}