package com.github.starter.app.ldap.repository

import org.testcontainers.containers.GenericContainer

class ContainerSupport(imageName: String): GenericContainer<ContainerSupport>(imageName)
