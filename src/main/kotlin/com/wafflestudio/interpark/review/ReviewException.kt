package com.wafflestudio.interpark.review

import com.wafflestudio.interpark.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class ReviewException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class ReviewNotFoundException : ReviewException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Review Not Found",
)

class ReviewPermissionDeniedException : ReviewException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Unauthorized Access To Review",
)

class ReviewContentLengthOutOfRangeException : ReviewException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Empty Or Too Long Content",
)

class ReviewRatingOutOfRangeException : ReviewException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Rating Out Of Range",
)
