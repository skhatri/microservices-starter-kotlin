package com.github.starter.app.config;

import com.github.starter.core.exception.ConfigurationException


class JdbcClientFactory(private val clients: Map<String, JdbcClient>) {

    fun forName(jdbcClientName: String): JdbcClient {
        return this.clients.getOrElse(jdbcClientName, fun(): JdbcClient =
            throw ConfigurationException("no jdbc client of name $jdbcClientName found"))
    }

}
