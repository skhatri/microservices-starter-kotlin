package com.github.starter.core.consumer

import reactor.core.publisher.Mono
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

class MonoConsumer<T>(private val mono: Mono<T>, private val error: Boolean) {

    fun drain(consumer: Consumer<T>?) {
        val latch = CountDownLatch(1)
        mono.subscribe({ res ->
                consumer?.accept(res)
                if (!error) {
                    latch.countDown()
                }
            },
            {
                if (error) {
                    latch.countDown()
                }
            })

        try {
            latch.await()
        } catch (ie: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    fun drain() {
        drain(null)
    }
}
