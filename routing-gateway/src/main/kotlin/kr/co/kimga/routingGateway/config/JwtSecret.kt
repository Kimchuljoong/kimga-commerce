package kr.co.kimga.routingGateway.kr.co.kimga.routingGateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtSecret(
    var access: JwtSecretConfig = JwtSecretConfig(),
    var refresh: JwtSecretConfig = JwtSecretConfig()
) {
    data class JwtSecretConfig(
        var secret: String = ""
    )
}