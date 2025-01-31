package com.wafflestudio.interpark

import com.wafflestudio.interpark.user.SocialAccountNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DomainException::class)
    fun handle(exception: DomainException): ResponseEntity<Map<String, Any>> {
        val responseBody = mutableMapOf(
            "error" to exception.msg,
            "errorCode" to exception.errorCode
        )

        if (exception is SocialAccountNotFoundException) {
            responseBody["provider"] = exception.provider.toString()
            responseBody["providerId"] = exception.providerId
        }

        return ResponseEntity
            .status(exception.httpErrorCode)
            .body(responseBody)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exeption: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = exeption.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity.badRequest().body(
            mapOf(
                "error" to "Method Argument Validation failed",
                "details" to errors
            )
        )
    }
}
