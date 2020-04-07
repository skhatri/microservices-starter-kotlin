package com.github.starter.app.config

import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory
import org.apache.directory.ldap.client.api.LdapConnection
import org.apache.directory.ldap.client.api.LdapConnectionConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["ldap.enabled"], havingValue = "true")
class LdapClient {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LdapClient::class.java)
    }

    @Autowired
    @Bean
    fun create(ldapConfig: LdapConfig): LdapConnection {
        val ldapConnectionConfig = ldapConnectionConfig(ldapConfig)
        val connectionFactory = DefaultLdapConnectionFactory(ldapConnectionConfig)
        connectionFactory.setTimeOut(ldapConfig.timeout)
        val connection = connectionFactory.newLdapConnection()
        connection.bind()
        return connection
    }

    private fun ldapConnectionConfig(ldapConfig: LdapConfig): LdapConnectionConfig {
        LOGGER.info("Creating ldap connection to host {}:{} with ssl = {}", ldapConfig.host, ldapConfig.port, ldapConfig.ssl)
        val ldapConnectionConfig = LdapConnectionConfig()
        ldapConnectionConfig.ldapHost = ldapConfig.host
        ldapConnectionConfig.ldapPort = ldapConfig.port
        ldapConnectionConfig.name = ldapConfig.username
        ldapConnectionConfig.credentials = ldapConfig.password
        ldapConnectionConfig.isUseSsl = ldapConfig.ssl
        return ldapConnectionConfig
    }

}
