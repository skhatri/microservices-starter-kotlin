package com.github.starter.app.ldap.endpoints

import com.github.starter.app.ldap.model.TodoTask
import com.github.starter.app.ldap.service.TodoService
import com.github.starter.core.container.Container
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/todo")
class TodoEndpoints(private val todoService: TodoService) {

    @GetMapping("/search")
    fun list(): Mono<Container<List<TodoTask>>> {
        return todoService.listItems().map { Container(it) }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): Mono<Container<TodoTask>> {
        return todoService.findById(id).map { Container(it) }
    }

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun add(@RequestBody todoTask: TodoTask): Mono<Container<TodoTask>> {
        return todoService.save(todoTask).map { Container(it) }
    }

    @PostMapping(value = ["/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@PathVariable("id") id: String, @RequestBody todoTask: TodoTask): Mono<Container<TodoTask>> {
        return todoService.update(id, todoTask).map { Container(it) }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Mono<Container<Map<String, Boolean>>> {
        return todoService.delete(id).map { b -> mapOf("result" to b) }.map { Container(it) }
    }
}
