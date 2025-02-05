package com.github.starter.app.todo.repository;

import com.github.starter.app.config.JdbcClientFactory
import com.github.starter.app.todo.model.TodoTask
import com.github.starter.core.exception.InternalServerError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*
import java.util.function.Predicate

@Repository
open class DefaultTodoRepository @Autowired constructor(
    clientFactory: JdbcClientFactory,
    @Value("\${flags.default-jdbc-client}") jdbcClientName: String
) : TodoRepository {

    private val databaseClient: DatabaseClient = clientFactory.forName(jdbcClientName).client()

    override fun listItems(): Mono<List<TodoTask>> {
        return databaseClient
            .sql("select * from todo.tasks limit 20").mapProperties(TodoTask::class.java)
            .all().collectList()
    }

    @Override
    override fun findById(id: String): Mono<TodoTask> {
        return databaseClient
            .sql("select * from todo.tasks where id= $1").bind("$1", id)
            .mapProperties(TodoTask::class.java)
            .first();
    }

    override fun add(todoTask: TodoTask): Mono<TodoTask> {
        val id: String = UUID.randomUUID().toString();
        val updated: LocalDateTime = LocalDateTime.now();
        return databaseClient
            .sql("insert into todo.tasks(id, description, action_by, created, status, updated) values($1, $2, $3, $4, $5, $6)")
            .bind("$1", id)
            .bind("$2", todoTask.description)
            .bind("$3", todoTask.actionBy ?: "")
            .bind("$4", LocalDateTime.now())
            .bind("$5", "NEW")
            .bind("$6", updated)
            .fetch().rowsUpdated()
            .filter(LongValuePredicate(1))
            .switchIfEmpty(
                Mono.error<Long>(
                    InternalServerError.fromCodeAndMessage(
                        "add-error",
                        "Could not add TODO record"
                    )
                )
            )
            .then(
                Mono.just(
                    TodoTask(
                        id,
                        todoTask.description,
                        todoTask.actionBy,
                        todoTask.created,
                        todoTask.status,
                        updated
                    )
                )
            );
    }

    @Override
    override fun update(todoTask: TodoTask): Mono<TodoTask> {
        val updatedTime: LocalDateTime = LocalDateTime.now();
        return databaseClient.sql("update todo.tasks set description=$1, action_by=$2, status=$3, updated=$4 where id=$5")
            .bind("$1", todoTask.description)
            .bind("$2", todoTask.actionBy ?: "")
            .bind("$3", todoTask.status ?: "NEW")
            .bind("$4", updatedTime)
            .bind("$5", todoTask.id).fetch().rowsUpdated()
            .filter(LongValuePredicate(1))
            .switchIfEmpty(
                Mono.error<Long>(
                    InternalServerError.fromCodeAndMessage(
                        "update-error",
                        "Could not update TODO record"
                    )
                )
            )
            .then(
                Mono.just(
                    TodoTask(
                        todoTask.id,
                        todoTask.description,
                        todoTask.actionBy,
                        todoTask.created,
                        todoTask.status,
                        updatedTime
                    )
                )
            );
    }

    @Override
    override fun delete(id: String): Mono<Boolean> {
        return databaseClient.sql("delete from todo.tasks where id = $1")
            .bind("$1", id).fetch().rowsUpdated()
            .filter(LongValuePredicate(1))
            .switchIfEmpty(
                Mono.error(
                    InternalServerError.fromCodeAndMessage(
                        "delete-error",
                        String.format("Could not delete TODO record %s", id)
                    )
                )
            )
            .then(Mono.just(true));
    }
}

class LongValuePredicate(private val excepted: Long) : Predicate<Long> {
    override fun test(t: Long): Boolean {
        return this.excepted == t
    }
}