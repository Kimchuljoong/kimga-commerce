package kr.co.kimga.routingGateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    var access = JwtSecretConfig()
    var refresh = JwtSecretConfig()

    class JwtSecretConfig {
        var secret: String = ""
        var exp: Long = 0
    }
}