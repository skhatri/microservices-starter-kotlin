package com.github.starter.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "datasource.jdbc")
@Component
open class JdbcProperties(val ref: List<ConfigItem>)
