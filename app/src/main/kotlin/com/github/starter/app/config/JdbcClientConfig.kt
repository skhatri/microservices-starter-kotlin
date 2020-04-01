package com.github.starter.app.config;

import com.github.starter.app.secrets.SecretsClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
@ConditionalOnProperty(name = ["datasource.jdbc.enabled"], havingValue = "true")
open class JdbcClientConfig {

    @Autowired
    @Bean
    open fun databaseProperties(jdbcProperties: JdbcProperties): Map<String, ConfigItem> {
        return jdbcProperties.ref.map { kv -> kv.name to kv }.toMap();
    }

    @Autowired
    @Bean
    open fun dataSources(jdbcConfigItems: Map<String, ConfigItem>, secretsClient: SecretsClient): JdbcClientFactory {
        return JdbcClientFactory(JdbcClientPreparator(jdbcConfigItems, secretsClient).configure(initScripts()));
    }

    private fun initScripts(): (ConfigItem, JdbcClient) -> Unit {
        return fun(configItem: ConfigItem, jdbcClient: JdbcClient) {
            if (configItem.driver == "h2") {
                configItem.load?.let { load ->
                    JdbcScriptProcessor().process(load, jdbcClient.create().block()!!)
                }
            }
        }
    }


}
