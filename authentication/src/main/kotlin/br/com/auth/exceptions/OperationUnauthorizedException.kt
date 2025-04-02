package br.com.auth.exceptions

import br.com.auth.utils.CoreUtils.OPERATION_UNAUTHORIZED
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class OperationUnauthorizedException(message: String = OPERATION_UNAUTHORIZED) : AuthenticationException(message)
