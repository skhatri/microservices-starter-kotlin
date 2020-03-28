package com.github.starter.core.exception;

 open class ApiException(val code: String, val summary: String, val status: Int, cause: Throwable?) : RuntimeException(code, cause) {
    protected constructor(code: String, summary: String, status: StatusCodes, cause: Throwable?) : this(code, summary, status.code, cause)
}
