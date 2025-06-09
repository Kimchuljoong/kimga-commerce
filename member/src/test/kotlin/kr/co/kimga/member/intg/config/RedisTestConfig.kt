package kr.co.kimga.member.intg.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class RedisTestConfig {
    companion object {
        private val redis = GenericContainer("redis:6.2.7-alpine")
            .withExposedPorts(6379)
            .withReuse(true)
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun registerRedisProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host") { redis.host }
            registry.add("spring.data.redis.port") { redis.getMappedPort(6379).toString() }
        }
    }
}
