package kr.co.kimga.routingGateway.config

import kr.co.kimga.routingGateway.provider.JwtProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtSecret::class)
class JwtConfig(
    private val jwtSecret: JwtSecret
) {

    fun jwtAccessProvider(): JwtProvider {
        return JwtProvider(secret = jwtSecret.access.secret)
    }

    fun jwtRefreshProvider(): JwtProvider {
        return JwtProvider(secret = jwtSecret.refresh.secret)
    }
}