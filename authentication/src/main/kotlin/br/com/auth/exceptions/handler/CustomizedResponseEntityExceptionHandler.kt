package br.com.auth.exceptions.handler

import br.com.auth.exceptions.ForbiddenActionRequestException
import br.com.auth.exceptions.InvalidJwtAuthenticationException
import br.com.auth.exceptions.ObjectDuplicateException
import br.com.auth.exceptions.OperationUnauthorizedException
import br.com.auth.exceptions.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAllException(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidJwtAuthenticationException::class)
    fun handleInvalidJwtAuthenticationExceptions(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ForbiddenActionRequestException::class)
    fun handleForbiddenActionRequestExceptions(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.FORBIDDEN)
    }

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

    @ExceptionHandler(OperationUnauthorizedException::class)
    fun handleOperationUnauthorizedExceptions(
        exception: Exception
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = exception.message
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.FORBIDDEN)
    }
}
