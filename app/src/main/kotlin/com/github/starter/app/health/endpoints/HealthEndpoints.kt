package com.github.starter.app.health.endpoints

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController(value = "/")
class HealthEndpoints {

    @GetMapping("/")
    fun index(): Mono<Map<String, Any>> {
        return createPayload("up", "Journey starts here!")
    }

    @GetMapping("/favicon.ico")
    fun favicon(): Mono<Void> {
        return Mono.empty()
    }

    @GetMapping("/liveness")
    fun liveness(): Mono<Map<String, Any>> {
        return createPayload("live", "is running!")
    }

    @GetMapping("/readiness")
    fun readiness(): Mono<Map<String, Any>> {
        return createPayload("ready", "can serve!")
    }

    private fun createPayload(status: String, message: String): Mono<Map<String, Any>> {
        return Mono.just(mapOf("status" to status, "message" to message))
    }

}
