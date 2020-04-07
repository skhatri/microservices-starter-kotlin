package com.github.starter.core.exception

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Exception Tests")
class ExceptionTests {

    @Test
    fun `Bad Request Exception Test`() {
        val badRequest = BadRequest()
        Assertions.assertEquals(400, badRequest.status)
        Assertions.assertNotNull(badRequest.code)
        Assertions.assertNotNull(badRequest.summary)
    }

    @Test
    fun `Internal Server Error Exception Test`() {
        val internalServerError = InternalServerError()
        Assertions.assertEquals(500, internalServerError.status)
        Assertions.assertNotNull(internalServerError.code)
        Assertions.assertNotNull(internalServerError.summary)
    }
}
