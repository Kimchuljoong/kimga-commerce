package kr.co.kimga.order.unit

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kr.co.kimga.order.infrastructure.repository.StockJpaRepository
import kr.co.kimga.order.infrastructure.service.order.OrderService
import kr.co.kimga.order.infrastructure.service.stock.StockService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class StockServiceTest {

    @MockK
    private lateinit var stockJpaRepository: StockJpaRepository

    @InjectMockKs
    private lateinit var stockService: StockService



}