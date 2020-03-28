package com.github.starter.app.todo.repository;

import com.github.starter.app.todo.model.TodoTask
import reactor.core.publisher.Mono

interface TodoRepository {
    fun listItems(): Mono<List<TodoTask>>;

    fun findById(id: String): Mono<TodoTask>;

    fun add(todoTask: TodoTask): Mono<TodoTask>;

    fun update(todoTask: TodoTask): Mono<TodoTask>;

    fun delete(id: String): Mono<Boolean>;
}
