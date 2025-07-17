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
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class StockServiceTest {

    @MockK
    private lateinit var stockJpaRepository: StockJpaRepository

    @InjectMockKs
    private lateinit var stockService: StockService

    @Test
    @DisplayName("상품에 대한 재고를 생성할 수 있다")
    fun `can create stock for product`() {
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

    @Test
    @DisplayName("상품의 재고를 차감할 수 있다")
    fun `can decrease product stock`() {

        // given
        val productId = 1L
        val quantity = 3L
        val totalQuantity = 10L

        val fakeStock = Stock(
            id = 1L,
            productId = productId,
            totalInventory = totalQuantity
        )

        every {
            stockJpaRepository.findStockByProductId(any())
        } returns fakeStock

        // when
        stockService.decreaseInventory(productId, quantity)

        // then
        assertEquals(totalQuantity - quantity, fakeStock.getAvailableInventory())
    }

    @Test
    @DisplayName("상품의 재고를 복원할 수 있다")
    fun `can restore product stock`() {

        // given
        val productId = 1L
        val quantity = 3L
        val totalQuantity = 10L

        val fakeStock = Stock(
            id = 1L,
            productId = productId,
            totalInventory = totalQuantity,
            orderedInventory = 10L
        )

        every {
            stockJpaRepository.findStockByProductId(any())
        } returns fakeStock

        // when
        stockService.restoreInventory(productId, quantity)

        // then
        assertEquals(quantity, fakeStock.getAvailableInventory())

    }

    @Test
    @DisplayName("상품 재고의 총 수량을 변경할 수 있다")
    fun `can apply totalInventory for product`() {

        // given
        val productId = 1L
        val quantity = 3L
        val totalQuantity = 0L

        val fakeStock = Stock(
            id = 1L,
            productId = productId,
            totalInventory = totalQuantity
        )

        every {
            stockJpaRepository.findStockByProductId(any())
        } returns fakeStock

        // when
        stockService.applyInventory(productId, quantity)

        // then
        assertEquals(quantity, fakeStock.getAvailableInventory())
    }

    @Test
    @DisplayName("상품의 현재 구매 가능한 재고를 조회할 수 있다")
    fun `can get available inventory for product`() {

        // given
        val productId = 1L
        val orderedInventory = 3L
        val totalQuantity = 10L

        val fakeStock = Stock(
            id = 1L,
            productId = productId,
            totalInventory = totalQuantity,
            orderedInventory = orderedInventory
        )

        every {
            stockJpaRepository.findStockByProductId(any())
        } returns fakeStock

        // when
        val availableProductInventory = stockService.getAvailableProductInventory(productId)

        // then
        assertEquals(totalQuantity - orderedInventory, availableProductInventory)
    }
}