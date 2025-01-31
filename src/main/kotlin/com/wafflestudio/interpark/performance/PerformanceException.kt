package com.wafflestudio.interpark.performance

import com.wafflestudio.interpark.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class PerformanceException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class PerformanceNotFoundException : PerformanceException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Performance Not Found",
)