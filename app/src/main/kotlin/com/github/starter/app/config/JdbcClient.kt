package com.github.starter.app.config;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

class JdbcClient(private val connectionFactory: ConnectionFactory) {

    fun create(): Mono<Connection> {
        return Mono.from(connectionFactory.create());
    }

    fun client(): DatabaseClient {
        return DatabaseClient.create(connectionFactory);
    }
}
