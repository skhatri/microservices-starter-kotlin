package com.github.starter.app.todo.repository

import org.testcontainers.containers.GenericContainer

class ContainerSupport(imageName: String): GenericContainer<ContainerSupport>(imageName)
