package com.github.starter.core.secrets

import com.github.skhatri.mounted.MountedSecretsFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SecretsConfig {

    @ConditionalOnProperty(name = ["secrets.enabled"], havingValue = "true", matchIfMissing = false)
    @Bean
    open fun createSecretClient(secretsProperties: SecretsProperties): SecretsClient {
        return SecretsClient(MountedSecretsFactory(secretsProperties.config).create())
    }

    @ConditionalOnProperty(name = ["secrets.enabled"], havingValue = "false", matchIfMissing = true)
    @Bean
    open fun createNoOpSecretClient(): SecretsClient {
        return SecretsClient(MountedSecretsFactory.noOpResolver())
    }
}