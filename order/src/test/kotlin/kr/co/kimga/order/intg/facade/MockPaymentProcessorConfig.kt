package kr.co.kimga.order.intg.facade

import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class MockPaymentProcessorConfig {

//    @Bean
//    fun mockPaymentProcessor(): PaymentProcessor {
//        val mock = mockk<PaymentProcessor>()
//        every {
//            mock.isSupported(any())
//        } returns true
//
//        every {
//            mock.process(any<RequestPayment>())
//        } returns PaymentResult(
//            result = "200",
//            transactionId = "11234",
//            approvedAt = Instant.now(),
//            mappedResult = PaymentProcessResult.PAID
//        )
//
//        every {
//            mock.cancelProcess(any<RequestCancelPayment>())
//        } returns CancelPaymentResult(
//            result = "201",
//            transactionId = "11235",
//            approvedAt = Instant.now(),
//            mappedResult = PaymentProcessResult.REFUNDED
//        )
//
//        return mock
//    }
}