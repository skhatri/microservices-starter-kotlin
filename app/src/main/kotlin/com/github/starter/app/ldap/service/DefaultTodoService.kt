package com.github.starter.app.ldap.service

import com.github.starter.app.ldap.model.TodoTask
import com.github.starter.app.ldap.repository.TodoRepository
import com.github.starter.core.exception.BadRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
open class DefaultTodoService(private val todoRepository: TodoRepository) : TodoService {

    @Override
    override fun listItems(): Mono<List<TodoTask>> {
        return todoRepository.listItems()
    }

    @Override
    override fun findById(id: String): Mono<TodoTask> {
        return todoRepository.findById(id)
    }

    @Override
    override fun save(task: TodoTask): Mono<TodoTask> {
        return todoRepository.add(task)
    }

    @Override
    override fun update(id: String, task: TodoTask): Mono<TodoTask> {
        if (id != task.id) {
            return Mono.error(BadRequest.Companion.forCodeAndMessage("invalid-id", "Provided Task ID does not match one in Payload"))
        }
        return todoRepository.update(task)
    }

    @Override
    override fun delete(id: String): Mono<Boolean> {
        return todoRepository.delete(id)
    }
}
