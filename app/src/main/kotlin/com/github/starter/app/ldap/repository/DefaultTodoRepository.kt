package com.github.starter.app.ldap.repository

import com.github.starter.app.config.JdbcClientFactory
import com.github.starter.app.ldap.model.TodoTask
import com.github.starter.core.exception.InternalServerError
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Predicate

@Repository
open class DefaultTodoRepository @Autowired constructor(clientFactory: JdbcClientFactory) : TodoRepository {

    private val databaseClient: DatabaseClient = clientFactory.forName("default-jdbc-client").client()

    override fun listItems(): Mono<List<TodoTask>> {
        return databaseClient
            .execute("select * from todo.tasks limit 20").`as`(TodoTask::class.java)
            .fetch().all().collectList()
    }

    @Override
    override fun findById(id: String): Mono<TodoTask> {
        return databaseClient
            .execute("select * from todo.tasks where id= $1").bind("$1", id)
            .`as`(TodoTask::class.java)
            .fetch().first()
    }

    override fun add(todoTask: TodoTask): Mono<TodoTask> {
        val id: String = UUID.randomUUID().toString()
        val updated: LocalDateTime = LocalDateTime.now()
        return databaseClient
            .execute("insert into todo.tasks(id, description, action_by, created, status, updated) values($1, $2, $3, $4, $5, $6)")
            .bind("$1", id)
            .bind("$2", todoTask.description)
            .bind("$3", todoTask.actionBy?:"")
            .bind("$4", LocalDateTime.now())
            .bind("$5", "NEW")
            .bind("$6", updated)
            .fetch().rowsUpdated()
            .filter (IntValuePredicate(1))
            .switchIfEmpty(Mono.error<Int>(InternalServerError.fromCodeAndMessage("add-error", "Could not add TODO record")))
            .then(Mono.just(TodoTask(id, todoTask.description, todoTask.actionBy, todoTask.created, todoTask.status, updated)))
    }

    @Override
    override fun update(todoTask: TodoTask): Mono<TodoTask> {
        val updatedTime: LocalDateTime = LocalDateTime.now()
        return databaseClient.execute("update todo.tasks set description=$1, action_by=$2, status=$3, updated=$4 where id=$5")
            .bind("$1", todoTask.description)
            .bind("$2", todoTask.actionBy?:"")
            .bind("$3", todoTask.status?:"NEW")
            .bind("$4", updatedTime)
            .bind("$5", todoTask.id).fetch().rowsUpdated()
            .filter (IntValuePredicate(1))
            .switchIfEmpty(Mono.error<Int>(InternalServerError.fromCodeAndMessage("update-error", "Could not update TODO record")))
            .then(Mono.just(TodoTask(todoTask.id, todoTask.description, todoTask.actionBy, todoTask.created, todoTask.status, updatedTime)))
    }

    @Override
    override fun delete(id: String): Mono<Boolean> {
        return databaseClient.delete().from("todo.tasks").matching(Criteria.where("id").`is`(id)).fetch().rowsUpdated()
            .filter (IntValuePredicate(1))
            .switchIfEmpty(Mono.error(InternalServerError.fromCodeAndMessage("delete-error", String.format("Could not delete TODO record %s", id))))
            .then(Mono.just(true))
    }
}

class IntValuePredicate(private val excepted:Int) : Predicate<Int> {
    override fun test(t: Int): Boolean {
        return this.excepted == t
    }
}