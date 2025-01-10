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

class ReservationNotFoundException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Reservation not found",
)

class ReservedAlreadyException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Reserved already exists",
)

class ReservedYetException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Not Reserved Yet",
)
class ReservationPermissionDeniedException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.FORBIDDEN,
    msg = "Reservation Permission denied",
)

class InValidHallTypeException : SeatException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Invalid Hall Type",
)