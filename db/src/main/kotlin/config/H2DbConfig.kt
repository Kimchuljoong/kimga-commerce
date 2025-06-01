package config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@Profile(value = ["local", "test"])
class H2DbConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "h2")
    fun datasource(): DataSource? {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource);
    }
}