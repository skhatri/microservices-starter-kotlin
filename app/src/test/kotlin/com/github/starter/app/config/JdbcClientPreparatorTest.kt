package com.github.starter.app.config;

import com.github.skhatri.mounted.MountedSecretsResolver
import com.github.starter.app.secrets.SecretsClient
import com.github.starter.core.exception.ConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito

@DisplayName("Jdbc Client Preparator")
class JdbcClientPreparatorTest {

    @Test
    fun `test no jdbc present`() {
        Assertions.assertThrows(ConfigurationException::class.java, Executable {
            val mountedSecretsResolver = Mockito.mock(MountedSecretsResolver::class.java)
            JdbcClientPreparator(mapOf(), SecretsClient(mountedSecretsResolver)).configure { _, _ -> }
        })
    }

}
