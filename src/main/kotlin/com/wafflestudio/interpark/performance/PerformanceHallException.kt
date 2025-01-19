package com.wafflestudio.interpark.performance

import com.wafflestudio.interpark.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class PerformanceHallException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class PerformanceHallNotFoundException : PerformanceHallException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "PerformanceHall Not Found",
)

class PerformanceHallNameConflictException : PerformanceHallException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "PerformanceHallName Conflict",
)