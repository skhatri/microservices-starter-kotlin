package com.github.starter.app.config

class ConfigItem {
    lateinit var name: String
    lateinit var driver: String
    lateinit var username: String
    lateinit var password: String

    var port: Int? = null
    var host: String? = null
    var protocol: String? = null

    var database: String? = null
    var enabled: Boolean = false
    var load: String? = null

}
