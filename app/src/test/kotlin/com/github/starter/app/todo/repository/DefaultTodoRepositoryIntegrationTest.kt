package com.github.starter.app.todo.repository;

import com.github.starter.app.config.ConfigItem
import com.github.starter.app.config.JdbcClientConfig
import com.github.starter.app.config.JdbcProperties
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
        cfg.password = "admin"
        cfg.name = "default-jdbc-client"
        cfg.database = "postgres"
        val jdbcProperties = JdbcProperties(listOf(cfg))

        val jdbcClientConfig = JdbcClientConfig()
        val config = jdbcClientConfig.databaseProperties(jdbcProperties)
        val factory = jdbcClientConfig.dataSources(config)

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
