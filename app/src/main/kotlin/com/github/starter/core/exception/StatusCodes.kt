package com.github.starter.core.exception

enum class StatusCodes(val code: Int) {
    OK(200), BAD_REQUEST(400), NOT_FOUND(404), UNAUTHORIZED(401), FORBIDDEN(403), INTERNAL_SERVER_ERROR(500)

    companion object {
        fun fromValue(value: Int): StatusCodes {
            return StatusCodes.values().find { statusCode -> statusCode.code == value } ?: INTERNAL_SERVER_ERROR
        }
    }
}
