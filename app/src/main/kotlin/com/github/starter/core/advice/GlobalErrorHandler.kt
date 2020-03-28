package com.github.starter.core.advice;

import com.github.starter.core.container.Container
import com.github.starter.core.container.MessageItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
@Order(-2)
open class GlobalErrorHandler @Autowired constructor(errorAttributes: CustomErrorAttributes,
                                                applicationContext: ApplicationContext,
                                                codecConfigurer: ServerCodecConfigurer) :
    AbstractErrorWebExceptionHandler(errorAttributes, ResourceProperties(), applicationContext) {
    init {
        super.setMessageWriters(codecConfigurer.writers);
        super.setMessageReaders(codecConfigurer.readers);
    }

    public override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), HandlerFunction<ServerResponse> { request ->
            val error = getErrorAttributes(request, false);

            val statusCode = Integer.parseInt((error["status"]?:"500").toString());
            val messageItem = MessageItem::class.java.cast(error["error"]);
            val container = Container<List<MessageItem>>(listOf(messageItem));
            ServerResponse.status(HttpStatus.valueOf(statusCode)).contentType(MediaType.APPLICATION_JSON).bodyValue(container);
        })
    }


}
