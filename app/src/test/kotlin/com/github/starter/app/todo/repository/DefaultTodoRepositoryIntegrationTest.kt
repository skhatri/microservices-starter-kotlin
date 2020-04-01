package com.github.starter.app.todo.repository;

import com.github.skhatri.mounted.MountedSecretsFactory
import com.github.skhatri.mounted.model.ErrorDecision
import com.github.skhatri.mounted.model.SecretConfiguration
import com.github.skhatri.mounted.model.SecretProvider
import com.github.starter.app.config.ConfigItem
import com.github.starter.app.config.JdbcClientConfig
import com.github.starter.app.config.JdbcProperties
import com.github.starter.app.secrets.SecretsClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DisplayName("Todo Repository Integration Tests")
@Testcontainers
class DefaultTodoRepositoryIntegrationTest {

    private var todoRepositoryUseCases: TodoRepositoryUseCases? = null

    @Container
    val postgres: ContainerSupport = ContainerSupport("skhatri/todo-postgres:11.5")
        .withEnv("POSTGRES_PASSWORD", "admin")
        .withExposedPorts(5432)

    @BeforeEach
    fun setUp() {

        val cfg = ConfigItem()

        cfg.driver = "postgresql"
        cfg.enabled = true
        cfg.host = postgres.containerIpAddress
        cfg.port = postgres.firstMappedPort
        cfg.username = "postgres"
        cfg.password = "secret::vault::postgres:-admin"
        cfg.name = "default-jdbc-client"
        cfg.database = "postgres"
        val jdbcProperties = JdbcProperties(listOf(cfg))

        val jdbcClientConfig = JdbcClientConfig()
        val config = jdbcClientConfig.databaseProperties(jdbcProperties)

        val secretProvider = SecretProvider()
        secretProvider.entriesLocation = "all.properties"
        secretProvider.errorDecision = ErrorDecision.EMPTY.toString().toLowerCase()
        secretProvider.isIgnoreResourceFailure = true
        secretProvider.name = "vault"
        secretProvider.mount = "/doesntexist"

        val secretConfiguration = SecretConfiguration()
        secretConfiguration.providers = listOf(secretProvider)
        val mountedSecretsResolver = MountedSecretsFactory(secretConfiguration).create()
        val secretsClient = SecretsClient(mountedSecretsResolver)
        val factory = jdbcClientConfig.dataSources(config, secretsClient)

        this.todoRepositoryUseCases = TodoRepositoryUseCases(DefaultTodoRepository(factory))
    }

    @DisplayName("list todo")
    @Test
    fun testListTodo() {
        todoRepositoryUseCases!!.testListTodoTasks()
    }

    @DisplayName("add todo")
    @Test
    fun testAddTodoTask() {
        todoRepositoryUseCases!!.verifyAddTodoTask()
    }


    @DisplayName("delete todo")
    @Test
    fun testDeleteTodoTask() {
        todoRepositoryUseCases!!.verifyDeleteTodoTask()
    }


    @DisplayName("update todo")
    @Test
    fun testUpdateTodoTask() {
        todoRepositoryUseCases!!.verifyUpdateTodoTask()
    }

    @AfterEach
    fun tearDown() {
        postgres.stop()
    }
}
