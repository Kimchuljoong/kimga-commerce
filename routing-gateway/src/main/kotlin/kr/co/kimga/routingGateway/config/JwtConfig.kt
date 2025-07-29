package kr.co.kimga.routingGateway.kr.co.kimga.routingGateway.config

import kr.co.kimga.routingGateway.provider.JwtProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtSecret::class)
class JwtConfig(
    private val jwtSecret: JwtSecret
) {

    @Bean
    fun jwtAccessProvider(): JwtProvider {
        return JwtProvider(secret = jwtSecret.access.secret)
    }

    @Bean
    fun jwtRefreshProvider(): JwtProvider {
        return JwtProvider(secret = jwtSecret.refresh.secret)
    }
}