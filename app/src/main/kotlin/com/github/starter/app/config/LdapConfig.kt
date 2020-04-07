package com.github.starter.app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "ldap")
class LdapConfig {
    lateinit var username: String
    lateinit var password: String
    lateinit var host: String
    lateinit var baseDn: String

    var port: Int = 636
    var timeout: Long = 0
    var ssl: Boolean = false
    var enabled: Boolean = false
}
