package br.com.auth.controller

import br.com.auth.entities.user.User
import br.com.auth.exceptions.ForbiddenActionRequestException
import br.com.auth.service.UserProfileService
import br.com.auth.utils.CoreUtils.APPLICATION_JSON
import br.com.auth.utils.CoreUtils.EMPTY_FIELDS
import br.com.auth.vo.user.ChangeInfoUserRequestVO
import br.com.auth.vo.user.ChangePasswordVO
import br.com.auth.vo.user.EmailRequestVO
import br.com.auth.vo.user.UserAuthenticatedResponseVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/user/profile/v1"])
@Tag(name = "User", description = "EndPoint For Managing Settings of User")
class UserProfileController {

    @Autowired
    private lateinit var userProfileService: UserProfileService

    @GetMapping(
        value = ["/authenticated"],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Find User Authenticated",
        description = "Find User Authenticated",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = UserAuthenticatedResponseVO::class)))
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
    fun findUserAuthenticated(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<UserAuthenticatedResponseVO> {
        return ResponseEntity.ok().body(userProfileService.findUserAuthenticated(user = user))
    }

    @PatchMapping(
        value = ["/change/info"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Change Info of User",
        description = "Change Info Of User",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = ChangeInfoUserRequestVO::class)))
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
    fun changeInfoUserLogged(
        @AuthenticationPrincipal user: User,
        @RequestBody info: ChangeInfoUserRequestVO
    ) {
        require(
            value = info.name.isNotBlank() && info.name.isNotEmpty() &&
                    info.surname.isNotBlank() && info.surname.isNotEmpty() &&
                    info.userName.isNotBlank() && info.userName.isNotEmpty()
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        ResponseEntity.ok(userProfileService.changeInfoUserLogged(user = user, info = info))
    }

    @PatchMapping(
        value = ["/change/email"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Change Email of User",
        description = "Change Email of User",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
                    Content(array = ArraySchema(schema = Schema(implementation = EmailRequestVO::class)))
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
    fun changeEmailUserLogged(
        @AuthenticationPrincipal user: User,
        @RequestBody emailRequestVO: EmailRequestVO
    ) {
        require(
            value = emailRequestVO.email.isNotBlank() && emailRequestVO.email.isNotEmpty()
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        ResponseEntity.ok(userProfileService.changeEmailUserLogged(user = user, emailRequestVO = emailRequestVO))
    }

    @PatchMapping(
        value = ["/change/password"],
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Change Password of User",
        description = "Change Password of User",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "Success", responseCode = "200", content = [
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
    fun changePasswordUserLogged(
        @AuthenticationPrincipal user: User,
        @RequestBody changePasswordVO: ChangePasswordVO
    ) {
        require(
            value = changePasswordVO.email.isNotBlank() && changePasswordVO.email.isNotEmpty() &&
                    changePasswordVO.password.isNotBlank() && changePasswordVO.password.isNotEmpty()
        ) {
            throw ForbiddenActionRequestException(message = EMPTY_FIELDS)
        }
        ResponseEntity.ok(userProfileService.changePasswordUserLogged(user = user, changePasswordVO = changePasswordVO))
    }

    @DeleteMapping(
        value = ["/delete/user"],
        produces = [APPLICATION_JSON]
    )
    @Operation(
        summary = "Delete User Common Account",
        description = "Delete User Common Account",
        tags = ["User"],
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
    fun deleteAccount(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<*> {
        userProfileService.deleteAccountUserLogged(user = user)
        return ResponseEntity.noContent().build<Any>()
    }
}
