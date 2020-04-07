package com.github.starter.core.exception

class InternalServerError(code: String, message: String) : ApiException(code, message, StatusCodes.INTERNAL_SERVER_ERROR, null) {
    constructor() : this("internal-error", "Internal Server Error")

    companion object {
        fun fromCodeAndMessage(code: String, message: String): InternalServerError {
            return InternalServerError(code, message)
        }
    }
}
