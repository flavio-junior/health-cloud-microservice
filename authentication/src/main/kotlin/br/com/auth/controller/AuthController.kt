package br.com.auth.controller

import br.com.auth.exceptions.ForbiddenActionRequestException
import br.com.auth.service.AuthService
import br.com.auth.utils.CoreUtils.APPLICATION_JSON
import br.com.auth.utils.CoreUtils.EMPTY_FIELDS
import br.com.auth.vo.user.EmailRequestVO
import br.com.auth.vo.user.NewPasswordRequestVO
import br.com.auth.vo.user.SignInRequestVO
import br.com.auth.vo.user.SignUpRequestVO
import br.com.auth.vo.user.TokenResponseVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/auth/v1"])
@Tag(name = "Auth", description = "EndPoint For Manager Authentication")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping(
        value = ["/confirm-email-address"],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Confirm Email",
        description = "Confirm Email",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "No Content", responseCode = "204", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = Unit::class)))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun confirmEmailAddress(
        @RequestBody emailRequestVO: EmailRequestVO
    ): ResponseEntity<*> {
        require(value = emailRequestVO.email.isNotEmpty() && emailRequestVO.email.isNotBlank()) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        authService.confirmEmailAddress(emailRequestVO = emailRequestVO)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping(
        value = ["/check-code-existent/{code}"],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Check Code Existent",
        description = "Check Code Existent",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "No Content", responseCode = "204", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = Unit::class)))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not Found", responseCode = "404", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun checkCodeSendToConfirmEmail(
        @PathVariable code: String
    ): ResponseEntity<*> {
        authService.checkCodeSendToConfirmEmail(code = code)
        return ResponseEntity.noContent().build<Any>()
    }

    @PostMapping(
        value = ["/signUp"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Create New User",
        description = "Create New User",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "Created", responseCode = "201", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Conflict", responseCode = "409", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun signUp(
        @RequestBody signUpRequestVO: SignUpRequestVO
    ): ResponseEntity<*> {
        require(
            value = signUpRequestVO.email.isNotEmpty() && signUpRequestVO.email.isNotBlank()
                    && signUpRequestVO.password.isNotEmpty() && signUpRequestVO.password.isNotBlank()
                    && signUpRequestVO.typeAccount != null
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        authService.signUp(signUpRequestVO = signUpRequestVO)
        return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    }

    @PostMapping(
        value = ["/signIn"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Authentication",
        description = "Authentication",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
                    Content(schema = Schema(implementation = TokenResponseVO::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun signIn(
        @RequestBody signInRequestVO: SignInRequestVO
    ): ResponseEntity<TokenResponseVO> {
        require(
            value = signInRequestVO.email.isNotEmpty() && signInRequestVO.email.isNotBlank() &&
                    signInRequestVO.password.isNotEmpty() && signInRequestVO.password.isNotBlank()
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        return ResponseEntity.ok(authService.signIn(signInRequestVO = signInRequestVO))
    }

    @PostMapping(
        value = ["/recover-password"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Recover Password",
        description = "Recover Password",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "No Content", responseCode = "204", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun createRecoverPassword(
        @RequestBody emailRequestVO: EmailRequestVO
    ): ResponseEntity<*> {
        require(value = emailRequestVO.email.isNotEmpty() && emailRequestVO.email.isNotBlank()) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        authService.createRecoverPassword(emailRequestVO = emailRequestVO)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping(
        value = ["/check-recover-password/{code}"],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Check Recover Password",
        description = "Check Recover Password",
        tags = ["Auth"], responses = [
            ApiResponse(
                description = "No Content", responseCode = "204", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = Unit::class)))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not Found", responseCode = "404", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun checkRecoverPassword(
        @PathVariable code: String
    ): ResponseEntity<*> {
        authService.checkRecoverPassword(code = code)
        return ResponseEntity.noContent().build<Any>()
    }

    @PutMapping(
        value = ["/new-password"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Create New Password",
        description = "Create New Password",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "No Content", responseCode = "204", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not Found", responseCode = "404", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun saveNewPassword(
        @RequestBody passwordRequestVO: NewPasswordRequestVO
    ): ResponseEntity<*> {
        require(
            value = passwordRequestVO.email.isNotEmpty()
                    && passwordRequestVO.email.isNotBlank()
                    && passwordRequestVO.password.isNotEmpty()
                    && passwordRequestVO.password.isNotBlank()
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        authService.saveNewPassword(passwordRequestVO = passwordRequestVO)
        return ResponseEntity.noContent().build<Any>()
    }

    @PutMapping(
        value = ["/refresh/{email}"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Refresh Token",
        description = "Refresh Token",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
                    Content(schema = Schema(implementation = TokenResponseVO::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Operation Unauthorized", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not Found", responseCode = "404", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun refreshToken(
        @PathVariable("email") email: String,
        @RequestHeader("Authorization") refreshToken: String,
    ): ResponseEntity<TokenResponseVO> {
        return ResponseEntity.ok(authService.refreshToken(email = email, refreshToken = refreshToken))
    }
}
