package com.github.starter.app.ldap.repository

import com.github.starter.app.ldap.endpoints.Todos
import com.github.starter.app.ldap.model.TodoTask
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions

class TodoRepositoryUseCases(private val todoRepository:TodoRepository) {

    fun testListTodoTasks() {
        val tasks = this.todoRepository.listItems().block()!!
        Assertions.assertFalse(tasks.isEmpty(), "todo table should have some data")
    }

    fun verifyAddTodoTask() {
        val task = Todos.createOne(LocalDateTime.now())
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        val storedTask = todoRepository.findById(taskId).block()!!
        Assertions.assertEquals(savedTask.description, storedTask.description)
    }

    fun verifyDeleteTodoTask() {
        val task = Todos.createOne(LocalDateTime.now())
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        Assertions.assertTrue(todoRepository.delete(taskId).block()!!)

        Assertions.assertFalse(todoRepository.delete(taskId).onErrorReturn(false).block()!!)
    }

    fun verifyUpdateTodoTask() {
        val task = Todos.createOne(LocalDateTime.now())
        val savedTask = todoRepository.add(task).block()!!
        Assertions.assertEquals(task.description, savedTask.description)

        val taskId = savedTask.id
        todoRepository.update(
            TodoTask(taskId, savedTask.description, "user1", LocalDateTime.now(),
                "DONE", LocalDateTime.now())
        ).block()

        val updatedTask = todoRepository.findById(taskId).block()!!
        Assertions.assertEquals("DONE", updatedTask.status)

    }
}
