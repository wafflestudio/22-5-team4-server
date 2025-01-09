package com.wafflestudio.interpark.seat

import com.wafflestudio.interpark.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class SeatException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class PerformanceEventNotFoundException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Performance Event not found",
)

class ConflictingHallException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Halls found or no Hall found",
)