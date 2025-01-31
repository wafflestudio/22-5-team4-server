package com.wafflestudio.interpark.user

import com.wafflestudio.interpark.DomainException
import com.wafflestudio.interpark.user.persistence.Provider
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class UserException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

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
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "User not found",
)

class SignInInvalidPasswordException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Invalid Password",
)

class UserIdentityNotFoundException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "UserIdentity not found",
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

class NoRefreshTokenException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Token not found",
)

class SocialAccountAlreadyLinkedException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Social Account already linked to another user",
)

class SocialAccountNotFoundException (
    val provider: Provider,
    val providerId: String,
): UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "This $provider account is not linked to local account",
)
