package com.github.starter.core.filters

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@ConditionalOnProperty(name = ["flags.log.requests"], havingValue = "true", matchIfMissing = true)
@Component
class RequestTimingFilter(@Value("\${flags.log.parameters:false}") val logParameters: Boolean) : WebFilter {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(RequestTimingFilter::class.java)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val start = System.currentTimeMillis()
        val path = exchange.request.path.toString()
        val params = StringBuilder()
        if (this.logParameters) {
            val pairs = exchange.request.queryParams.toSingleValueMap()
                .toList().joinToString(", ") { em -> String.format("%s=%s", em.first, em.second) }
            if(pairs.isNotEmpty()) {
                params.append(", ")
            }
            params.append(pairs)
        }
        return chain.filter(exchange)
            .doOnSuccess {
                val endTime = System.currentTimeMillis()
                if (LOGGER.isInfoEnabled) {
                    LOGGER.info("tag={}, uri=\"{}\", time={}, unit=ms{}", "request-timing", path, (endTime - start), params)
                }
            }
    }


}
