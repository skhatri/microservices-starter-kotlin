package com.github.starter.core.advice

import com.github.starter.core.container.MessageItem
import com.github.starter.core.exception.ApiException
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class CustomErrorAttributes : DefaultErrorAttributes() {
    private val STATUS_KEY = "status"

    override fun getErrorAttributes(request: ServerRequest, includeStackTrace: Boolean): MutableMap<String, Any> {
        val errorMap = super.getErrorAttributes(request, includeStackTrace)
        val exception = getError(request)
        val builder = MessageItem.Builder()
        builder.withDetailItem("path", request.exchange().request.path.toString())

        if (exception is ApiException) {
            errorMap[STATUS_KEY] = exception.status
            builder.withDetailItem(STATUS_KEY, exception.status)
                .withCode(exception.code).withMessage(exception.summary)
        } else {
            val defaultStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
            errorMap[STATUS_KEY] = defaultStatusCode
            val ex = exception.cause ?: exception
            builder.withDetailItem(STATUS_KEY, defaultStatusCode).withCode(ex::class.java.simpleName).withMessage(ex.message?:"$ex")
        }
        errorMap["error"] = builder.build()
        return errorMap
    }
}
