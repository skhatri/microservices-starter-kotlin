package com.github.starter.app.config;

import com.github.starter.core.exception.ConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable

@DisplayName("Jdbc Client Preparator")
class JdbcClientPreparatorTest {

    @Test
    fun `test no jdbc present`() {
        Assertions.assertThrows(ConfigurationException::class.java, Executable {
            JdbcClientPreparator(mapOf()).configure { _, _ -> }
        })
    }

}
