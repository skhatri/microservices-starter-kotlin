package com.github.starter.app.health.endpoints

import com.github.starter.core.consumer.MonoConsumer
import com.github.starter.core.filters.RequestTimingFilters
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.function.Consumer


@DisplayName("Health Endpoints Test")
@ExtendWith(SpringExtension::class)
class HealthEndpointsTest {

    @Test
    fun `test liveness endpoint`() {
        val uri = "/liveness"
        val webTestClient:WebTestClient = WebTestClient.bindToController(HealthEndpoints::class.java)
            .webFilter<WebTestClient.ControllerSpec>(RequestTimingFilters.newInstance(true)).build()
        verifyResult(uri, webTestClient, Map::class.java) { m -> m.isNotEmpty()}
    }

    private fun <T> verifyResult(uri:String, webTestClient: WebTestClient, clz:Class<T>, predicate: (T)->Boolean) {
        val result:Mono<T> = Mono.from(webTestClient.get().uri(uri).exchange().expectStatus().isOk.returnResult(clz).responseBody)
        MonoConsumer(result, false).drain(Consumer {res ->
            Assertions.assertTrue(predicate(res))
        })
        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `test readiness endpoint`() {
        val uri = "/readiness"
        val webTestClient = WebTestClient.bindToController(HealthEndpoints::class.java)
            .webFilter<WebTestClient.ControllerSpec>(RequestTimingFilters.newInstance(false))
            .build()
        verifyResult(uri, webTestClient, Map::class.java) { m -> m.isNotEmpty() }
    }

    @Test
    fun `test index endpoint`() {
        val uri = "/"
        val webTestClient = WebTestClient.bindToController(HealthEndpoints::class.java).build()
        verifyResult(uri, webTestClient, Map::class.java) { m -> m.isNotEmpty()}
    }


    @Test
    fun `test non-existent endpoint`() {
        val uri = "/readiness-xyz?action=reload"
        val webTestClient = WebTestClient.bindToController(HealthEndpoints::class.java)
            .webFilter<WebTestClient.ControllerSpec>(RequestTimingFilters.newInstance(true))
            .build()
        webTestClient.get().uri(uri).exchange().expectStatus().isNotFound
    }
}
