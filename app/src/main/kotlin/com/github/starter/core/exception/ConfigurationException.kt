package com.github.starter.core.exception

class ConfigurationException(reason: String, cause: Throwable?) : RuntimeException(reason, cause) {
    constructor(reason: String) : this(reason, null)
}
