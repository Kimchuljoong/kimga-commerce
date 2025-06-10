package kr.co.kimga.product.unit

import kr.co.kimga.product.infrastructure.repository.ProductQuerydslRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(ProductQuerydslRepository::class)
class ProductRepositoryTest {

    @Test
    @DisplayName("상품 상태가 판매중인 상품들을 페이지로 조회할 수 있다")
    fun `can fetch products on sales with page`() {

    }
}