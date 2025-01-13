package com.wafflestudio.interpark.performance

import com.wafflestudio.interpark.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class PerformanceEventException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class PerformanceEventNotFoundException : PerformanceEventException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "PerformanceEvent Not Found",
)