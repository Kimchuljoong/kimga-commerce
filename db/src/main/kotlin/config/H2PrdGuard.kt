package config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.EnumerablePropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@Profile("!local & !test")
class H2PrdGuard(
    private val environment: Environment
) {

    @PostConstruct
    fun validate() {
        val propertyNames = (environment as ConfigurableEnvironment).propertySources
            .flatMap { propertySource ->
                when (propertySource) {
                    is EnumerablePropertySource<*> -> propertySource.propertyNames.asSequence()
                    else -> emptySequence()
                }
            }
            .filter { it.matches(Regex("^spring\\.datasource(\\.[^.]+)?\\.url$")) }
            .toSet()

        for (propertyName in propertyNames) {
            val url = environment.getProperty(propertyName) ?: continue
            if (url.contains("h2", ignoreCase = true)) {
                throw IllegalStateException("❌ H2는 운영환경에서 사용할 수 없습니다: $propertyName = $url")
            }
        }
    }
}