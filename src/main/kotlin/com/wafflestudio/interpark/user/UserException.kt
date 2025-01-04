package com.wafflestudio.interpark.user

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class UserException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : RuntimeException(msg, cause)

class SignUpBadUsernameException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Bad Username",
)

class SignUpBadPasswordException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Bad Password",
)

class SignUpUsernameConflictException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Username Conflict",
)

class SignInUserNotFoundException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "User not found",
)

class SignInInvalidPasswordException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Invalid Password",
)

class AuthenticateException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Unauthorized",
)

class TokenExpiredException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Token Expired",
)