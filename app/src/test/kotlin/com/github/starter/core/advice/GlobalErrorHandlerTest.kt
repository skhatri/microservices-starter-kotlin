package com.github.starter.core.advice

import com.github.starter.core.exception.BadRequest
import com.github.starter.core.exception.InternalServerError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.http.server.RequestPath
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Stream

@DisplayName("Global Error Handler Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlobalErrorHandlerTest {

    @ParameterizedTest(name = "Test Routing Function [{index}] {argumentsWithNames}")
    @MethodSource("data")
    fun testRoutingFunction(err: Throwable, expectedStatusCode: Int) {
        val applicationContext = Mockito.mock(ApplicationContext::class.java)
        Mockito.`when`(applicationContext.classLoader).thenReturn(javaClass.classLoader)
        val codecConfigurer = DefaultServerCodecConfigurer()
        val attributes = CustomErrorAttributes()
        val errorHandler = GlobalErrorHandler(attributes, applicationContext, codecConfigurer)
        errorHandler.afterPropertiesSet()

        val request = createErrorServerRequest(err)

        val serverResponse: Mono<ServerResponse> = errorHandler.getRoutingFunction(attributes).route(request).flatMap({ fn -> fn.handle(request) })
        val latch = CountDownLatch(1)
        serverResponse.subscribe(Consumer { res ->
            Assertions.assertEquals(expectedStatusCode, res.rawStatusCode())
            latch.countDown()
        })
        latch.await()

        StepVerifier.create(serverResponse)
            .thenConsumeWhile(Predicate { sr -> sr.statusCode().isError })
            .verifyComplete()
    }

    private fun createErrorServerRequest(throwable: Throwable): ServerRequest {
        val serverWebExchange = Mockito.mock(ServerWebExchange::class.java)
        val httpRequest = Mockito.mock(ServerHttpRequest::class.java)
        Mockito.`when`(httpRequest.id).thenReturn("request1")
        val requestPath = Mockito.mock(RequestPath::class.java)
        Mockito.`when`(requestPath.toString()).thenReturn("/some-uri")
        Mockito.`when`(httpRequest.path).thenReturn(requestPath)
        Mockito.`when`(serverWebExchange.request).thenReturn(httpRequest)
        return MockServerRequest.builder()
            .exchange(serverWebExchange)
            .attribute(DefaultErrorAttributes::class.java.name + ".ERROR", throwable)
            .build()
    }


    private fun data(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(BadRequest(), 400),
            Arguments.of(InternalServerError(), 500),
            Arguments.of(RuntimeException(), 500)
        )
    }
}
