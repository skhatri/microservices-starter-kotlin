package com.github.starter.app.todo.repository;

import com.github.starter.Application
import com.github.starter.app.todo.endpoints.Todos
import com.github.starter.app.todo.model.TodoTask
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ActiveProfiles("test")
@SpringBootTest(classes = [Application::class])
class DefaultTodoRepositoryTest @Autowired constructor(private val todoRepository: TodoRepository) {

    @Test
    fun `test list todo`() {
        val tasks = this.todoRepository.listItems().block()!!
        Assertions.assertFalse(tasks.isEmpty(), "todo table should have some data")
    }

    @Test
    fun `add todo`() {
        val task = Todos.createOne(LocalDateTime.now())
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        val storedTask = todoRepository.findById(taskId).block()!!
        Assertions.assertEquals(savedTask.description, storedTask.description)
    }


    @Test
    fun `delete todo`() {
        val task = Todos.createOne(LocalDateTime.now())
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        Assertions.assertTrue(todoRepository.delete(taskId).block()!!)

        Assertions.assertFalse(todoRepository.delete(taskId).onErrorReturn(false).block()!!)
    }


    @Test
    fun `update todo`() {
        val task = Todos.createOne(LocalDateTime.now());
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        todoRepository.update(
            TodoTask(taskId, savedTask.description, "user1", LocalDateTime.now(),
                "DONE", LocalDateTime.now())
        ).block();

        val updatedTask = todoRepository.findById(taskId).block()!!
        Assertions.assertEquals("DONE", updatedTask.status)

    }
}
