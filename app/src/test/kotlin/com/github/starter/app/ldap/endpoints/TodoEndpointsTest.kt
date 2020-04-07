package com.github.starter.app.ldap.endpoints

import com.github.starter.app.ldap.model.TodoTask
import com.github.starter.app.ldap.service.TodoService
import com.github.starter.core.advice.CustomErrorAttributes
import com.github.starter.core.advice.GlobalErrorHandler
import com.github.starter.core.consumer.MonoConsumer
import com.github.starter.core.exception.InternalServerError
import java.util.stream.Stream
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpMethod
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@DisplayName("Todo Endpoints")
@ExtendWith(value = [SpringExtension::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TodoEndpointsTest(@org.springframework.beans.factory.annotation.Autowired val applicationContext: ApplicationContext) {

    @ParameterizedTest(name = "Error Scenario - [{index}] {0} - {4} {1}")
    @MethodSource("data")
    fun testTodosErrorService(scenarioName: String, uri: String, serviceHook: (TodoService) -> Unit, clz: Class<Any>, method: HttpMethod) {
        verifyInternalServiceErrorResponse(uri, serviceHook, clz, method)
    }

    private fun <R> verifyInternalServiceErrorResponse(uri: String, serviceHook: (TodoService) -> Unit, clz: Class<R>, method: HttpMethod) {
        val todoService = Mockito.mock(TodoService::class.java)
        serviceHook.invoke(todoService)

        val todo = TodoEndpoints(todoService)
        val errorAttributes = CustomErrorAttributes()
        val globalErrorHandler = GlobalErrorHandler(errorAttributes, applicationContext, DefaultServerCodecConfigurer())
        val webTestClient = WebTestClient.bindToController(todo, globalErrorHandler).build()

        val result: Mono<R> = Mono.from(webTestClient.method(method).uri(uri).exchange().expectStatus().is5xxServerError.returnResult(clz).responseBody)
        MonoConsumer(result, false).drain()
        StepVerifier.create(result).expectComplete().verify()
    }

    private fun data(): Stream<Arguments> {
        val todoTask = Todos.createOneForToday()
        val id = todoTask.id
        return Stream.of(
            Arguments.of(
                "Find Todos test", "/todo/123",
                { todoService: TodoService -> Mockito.`when`(todoService.findById("123")).thenReturn(Mono.error<TodoTask>(InternalServerError())) }, Map::class.java, HttpMethod.GET
            ),
            Arguments.of(
                "Delete Todos id", "/todos/123",
                { todoService: TodoService -> Mockito.`when`(todoService.delete("123")).thenReturn(Mono.error<Boolean>(InternalServerError())) }, Map::class.java, HttpMethod.DELETE
            ),
            Arguments.of(
                "Update Todos", String.format("/todos/%s", id),
                { todoService: TodoService -> Mockito.`when`(todoService.update(id, todoTask)).thenReturn(Mono.error<TodoTask>(InternalServerError())) }, Map::class.java, HttpMethod.POST
            ),
            Arguments.of(
                "Add Todos", "/todos/",
                { todoService: TodoService -> Mockito.`when`(todoService.save(todoTask)).thenReturn(Mono.error<TodoTask>(InternalServerError())) }, Map::class.java, HttpMethod.POST
            ),
            Arguments.of(
                "Search Todos", "/todos/search",
                { todoService: TodoService -> Mockito.`when`(todoService.listItems()).thenReturn(Mono.error<List<TodoTask>>(InternalServerError())) }, Map::class.java, HttpMethod.GET
            )
        )
    }

}
