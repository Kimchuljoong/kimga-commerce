package kr.co.kimga.member.interfaces.config

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer

open class RedisTestConfig {
    companion object {
        private var redis: GenericContainer<*>
        private val REDIS_IMAGE_NAME = "redis:6.2.7-alpine"
        private val PORT = 6379

        init {
            redis = GenericContainer(REDIS_IMAGE_NAME)
                .withExposedPorts(PORT)
                .withReuse(true)
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            if (!redis.isRunning()) {
                redis.start()
            }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            redis.close()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redis::getHost)
            registry.add("spring.data.redis.port", redis::getFirstMappedPort)
        }
    }
}