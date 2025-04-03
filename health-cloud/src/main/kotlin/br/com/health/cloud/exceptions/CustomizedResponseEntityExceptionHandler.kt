package br.com.health.cloud.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectDuplicateException::class)
    fun handleObjectDuplicateException(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.CONFLICT.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT)
    }
}
