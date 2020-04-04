package com.github.starter.core.secrets

import com.github.skhatri.mounted.MountedSecretsResolver
import com.github.starter.core.exception.ConfigurationException


class SecretsClient(private val mountedSecretsResolver: MountedSecretsResolver) {

    fun resolve(key: String): CharArray {
        val secretValue = this.mountedSecretsResolver.resolve(key)
        if (secretValue.hasValue()) {
            return secretValue.value.get()
        }
        throw ConfigurationException("secret $key could not be resolved.")
    }
}