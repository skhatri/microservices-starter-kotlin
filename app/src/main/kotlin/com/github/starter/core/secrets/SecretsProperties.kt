package com.github.starter.core.secrets

import com.github.skhatri.mounted.model.SecretConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "secrets")
@Component
class SecretsProperties(var config: SecretConfiguration? = null)
