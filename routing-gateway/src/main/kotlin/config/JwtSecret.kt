package kr.co.kimga.routingGateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtSecret {
    var access = JwtSecretConfig()
    var refresh = JwtSecretConfig()

    class JwtSecretConfig {
        var secret: String = ""
        var exp: Long = 0
    }
}