package kr.co.kimga.member.intg.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class RedisTestConfig {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun registerRedisProperties(registry: DynamicPropertyRegistry) {
            val redis = RedisContainer.container
            registry.add("spring.data.redis.host") { redis.host }
            registry.add("spring.data.redis.port") { redis.getMappedPort(6379).toString() }
        }
    }
}

object RedisContainer {
    val container: GenericContainer<*> = GenericContainer("redis:6.2.7-alpine")
        .withExposedPorts(6379)
        .withReuse(true)
        .apply { start() }
}
