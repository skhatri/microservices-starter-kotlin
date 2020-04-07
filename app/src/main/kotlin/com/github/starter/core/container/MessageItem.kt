package com.github.starter.core.container

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class MessageItem @JsonCreator constructor(@JsonProperty("code") val code: String, @JsonProperty("message") val message: String,
                                           @JsonProperty("details")
                                           @JsonInclude(JsonInclude.Include.NON_EMPTY)
                                           val details: Map<String, Any>) {

    @JsonCreator
    constructor(code: String, message: String) : this(code, message, mapOf())


    class Builder(var code: String? = null, var message: String? = null, var details: Map<String, Any> = mapOf()) {

        fun withCode(code: String): Builder {
            this.code = code
            return this
        }

        fun withMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun withDetails(details: Map<String, Any>): Builder {
            this.details = this.details.plus(details)
            return this
        }

        fun withDetailItem(item: String, value: Any): Builder {
            this.details = this.details.plus(Pair(item, value))
            return this
        }

        fun build(): MessageItem {
            return MessageItem(code!!, message!!, details)
        }
    }
}
