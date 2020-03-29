package com.github.starter.app.todo.repository;

import com.github.starter.Application
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [Application::class])
class DefaultTodoRepositoryTest @Autowired constructor(private val todoRepository: TodoRepository) {

    private val todoRepositoryUseCases: TodoRepositoryUseCases = TodoRepositoryUseCases(todoRepository)

    @Test
    fun `test list todo`() {
        todoRepositoryUseCases.testListTodoTasks()
    }

    @Test
    fun `add todo`() {
        todoRepositoryUseCases.verifyAddTodoTask()
    }


    @Test
    fun `delete todo`() {
        todoRepositoryUseCases.verifyDeleteTodoTask()
    }


    @Test
    fun `update todo`() {
        todoRepositoryUseCases.verifyUpdateTodoTask()
    }
}
