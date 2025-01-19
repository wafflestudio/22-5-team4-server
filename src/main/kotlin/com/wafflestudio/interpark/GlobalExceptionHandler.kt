package com.wafflestudio.interpark

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DomainException::class)
    fun handle(exception: DomainException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(exception.httpErrorCode)
            .body(mapOf("error" to exception.msg, "errorCode" to exception.errorCode))
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
