package kr.co.kimga.product.infrastructure.repository

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.domain.entity.Product
import kr.co.kimga.product.domain.entity.QProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductQuerydslRepository(
    private val entityManager: EntityManager
) {

    private val queryFactory = JPAQueryFactory(entityManager)

    fun findProductsOnSale(productName: String, pageable: Pageable): Page<Product> {
        val product = QProduct.product

        val query = queryFactory
            .selectFrom(product)
            .where(
                productName.takeIf { it.isNotBlank() }.let {
                    product.productName.like("%$productName%")
                },
                product.productStatus.eq(ProductStatus.SALE)
            )

        val fetchedProducts = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(
                OrderSpecifier(Order.DESC, product.id)
            )
            .fetch()

        val total = queryFactory
            .select(product.count())
            .from(product)
            .fetchOne() ?: 0L

        return PageImpl(fetchedProducts, pageable, total)
    }

}