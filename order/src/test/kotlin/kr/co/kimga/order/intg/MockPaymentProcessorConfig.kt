package kr.co.kimga.order.intg

import io.mockk.every
import io.mockk.mockk
import kr.co.kimga.order.infrastructure.service.payment.dto.CancelPaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.PaymentResult
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestCancelPayment
import kr.co.kimga.order.infrastructure.service.payment.dto.RequestPayment
import kr.co.kimga.order.infrastructure.service.payment.pg.PaymentProcessor
import kr.co.kimga.order.infrastructure.service.payment.pg.TossPaymentProcessor
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.time.Instant

@TestConfiguration
class MockPaymentProcessorConfig {

    @Bean
    fun mockPaymentProcessor(): PaymentProcessor {
        val mock = mockk<PaymentProcessor>()
        every {
            mock.isSupported(any())
        } returns true

        every {
            mock.process(any<RequestPayment>())
        } returns PaymentResult(
            result = "200",
            transactionId = "11234",
            approvedAt = Instant.now()
        )

        every {
            mock.cancelProcess(any<RequestCancelPayment>())
        } returns CancelPaymentResult(
            result = "200",
            transactionId = "11235",
            approvedAt = Instant.now()
        )

        return mock
    }
}