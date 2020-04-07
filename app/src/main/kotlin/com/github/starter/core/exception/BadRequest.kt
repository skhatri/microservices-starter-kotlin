package com.github.starter.core.exception

class BadRequest(code: String, message: String) : ApiException(code, message, StatusCodes.BAD_REQUEST, null) {
    constructor() : this("bad-request", "Invalid Request")

    companion object {
        fun forCodeAndMessage(code: String, message: String): BadRequest {
            return BadRequest(code, message)
        }
    }
}
