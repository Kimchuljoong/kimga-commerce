package kr.co.kimga.order.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kr.co.kimga.order.domain.entity.stock.Stock
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.stock.StockService
import kr.co.kimga.order.infrastructure.service.stock.dto.RequestCreateStockDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class StockServiceTest {

    @MockK
    private lateinit var stockJpaRepository: StockJpaRepository

    @InjectMockKs
    private lateinit var stockService: StockService

    @Test
    @DisplayName("상품에 대한 재고를 생성할 수 있다")
    fun `Can Create Stock for Product`() {
        // given
        val productId = 1L

        val requestCreateStockDto = RequestCreateStockDto(
            productId = productId
        )

        val fakeStock = Stock(
            id = 1L,
            productId = productId,
        )

        every {
            stockJpaRepository.findStockByProductId(any())
        } returns null

        every {
            stockJpaRepository.save(any())
        } returns fakeStock

        // when
        stockService.createStock(requestCreateStockDto)

        // then
        verify { stockJpaRepository.findStockByProductId(any()) }
        verify { stockJpaRepository.save(any()) }
    }



}