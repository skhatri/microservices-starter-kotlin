package com.github.starter.app.config;

import com.github.starter.core.exception.ConfigurationException
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import java.util.function.BiConsumer;

class JdbcClientPreparator(private val configItemMap: Map<String, ConfigItem>) {

    fun configure(setupHook: (ConfigItem, JdbcClient) -> Unit): Map<String, JdbcClient> {

        val clients: Map<String, JdbcClient> = this.configItemMap
            .filter { kv -> kv.value.enabled }
            .map { kv ->
                val configItem = kv.value
                val options = ConnectionFactoryOptions.builder()
                options.option(ConnectionFactoryOptions.DRIVER, configItem.driver)

                configItem.database?.let { db -> options.option(ConnectionFactoryOptions.DATABASE, db) }
                configItem.host?.let{host -> options.option(ConnectionFactoryOptions.HOST, host)}
                configItem.port?.let{port -> options.option(ConnectionFactoryOptions.PORT, port)}
                options.option(ConnectionFactoryOptions.PASSWORD, configItem.password)
                options.option(ConnectionFactoryOptions.USER, configItem.username)
                configItem.protocol?.let { proto -> options.option(ConnectionFactoryOptions.PROTOCOL, proto)}

                val connFactory = ConnectionFactories.get(options.build())
                val jdbcClient = JdbcClient(connFactory)
                setupHook.invoke(configItem, jdbcClient)
                kv.key to jdbcClient
            }.toMap()
        if (clients.isEmpty()) {
            throw ConfigurationException("no jdbc clients are set. set datasource.jdbc.enabled=false if not using jdbc")
        }
        return clients;
    }
}
