package br.com.auth.service

import br.com.auth.entities.user.User
import br.com.auth.exceptions.ObjectDuplicateException
import br.com.auth.exceptions.ResourceNotFoundException
import br.com.auth.repository.UserRepository
import br.com.auth.utils.ConverterUtils.parseObject
import br.com.auth.utils.CoreUtils.THE_EMAIL_ALREADY_EXISTS
import br.com.auth.vo.user.ChangeInfoUserRequestVO
import br.com.auth.vo.user.ChangePasswordVO
import br.com.auth.vo.user.EmailRequestVO
import br.com.auth.vo.user.UserAuthenticatedResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userService: UserService

    @Transactional(readOnly = true)
    fun getUserLogged(
        userId: Long
    ): User {
        val userSaved: User? =
            userRepository.findById(userId).orElseThrow { ResourceNotFoundException(message = USER_NOT_FOUND) }
        if (userSaved != null) {
            return userSaved
        } else {
            throw ResourceNotFoundException(message = USER_NOT_FOUND)
        }
    }

    fun findUserAuthenticated(
        user: User
    ): UserAuthenticatedResponseVO {
        return parseObject(origin = user, destination = UserAuthenticatedResponseVO::class.java)
    }

    @Transactional
    fun changeInfoUserLogged(
        user: User,
        info: ChangeInfoUserRequestVO
    ) {
        getUserLogged(userId = user.id)
        val checkUserNameExisting: User? = userRepository.checkUsernameAlreadyExisting(userName = info.userName)
        if (checkUserNameExisting != null) {
            throw ObjectDuplicateException(message = THE_USERNAME_ALREADY_EXISTS)
        } else {
            userRepository.changeInfoUserLogged(
                userId = user.id,
                name = info.name,
                surname = info.surname,
                userName = info.userName
            )
        }
    }

    @Transactional
    fun changeEmailUserLogged(
        user: User,
        emailRequestVO: EmailRequestVO
    ) {
        getUserLogged(userId = user.id)
        val checkEmailAlreadyExisting: User? = userRepository.checkEmailAlreadyExisting(emailRequestVO.email)
        if (checkEmailAlreadyExisting != null) {
            throw ObjectDuplicateException(message = THE_EMAIL_ALREADY_EXISTS)
        } else {
            userRepository.changeEmailUserLogged(userId = user.id, email = emailRequestVO.email)
        }
    }

    @Transactional
    fun changePasswordUserLogged(
        user: User,
        changePasswordVO: ChangePasswordVO
    ) {
        val userSaved: User? =
            userRepository.fetchUserLoggedByIdAndEmail(userId = user.id, email = changePasswordVO.email)
        if (userSaved != null) {
            userRepository.changePasswordUserLogged(
                userId = user.id,
                password = passwordEncoder.encode(changePasswordVO.password)
            )
        } else {
            throw ResourceNotFoundException(message = USER_NOT_FOUND)
        }
    }

    @Transactional
    fun deleteAccountUserLogged(
        user: User
    ) {
        userService.deleteUser(userLoggedId = user.id)
    }

    companion object {
        const val USER_NOT_FOUND = "User Not Found!"
        const val THE_USERNAME_ALREADY_EXISTS = "The Username Already Exists!"
    }
}
