package kr.co.kimga.member.intg.config

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class RedisTestConfig {

    companion object {
        @JvmStatic
        @Container
        var redis: GenericContainer<*> = GenericContainer("redis:6.2.7-alpine")
            .withExposedPorts(6379)
            .withReuse(true)

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            if (redis.isRunning) {
                redis.start()
            }
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            redis.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redis::getHost)
            registry.add("spring.data.redis.port", redis::getFirstMappedPort)
        }
    }
}