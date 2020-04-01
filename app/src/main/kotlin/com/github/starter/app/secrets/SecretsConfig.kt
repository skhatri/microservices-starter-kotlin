package com.github.starter.app.secrets

import com.github.skhatri.mounted.MountedSecretsFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SecretsConfig {

    @Autowired
    @Bean
    open fun createSecretClient(secretsProperties: SecretsProperties): SecretsClient {
        return SecretsClient(MountedSecretsFactory(secretsProperties.config).create())
    }
}