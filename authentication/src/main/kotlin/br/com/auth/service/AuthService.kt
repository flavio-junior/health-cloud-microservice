package br.com.auth.service

import br.com.auth.entities.security.RecoverPassword
import br.com.auth.entities.security.Security
import br.com.auth.entities.user.User
import br.com.auth.exceptions.ForbiddenActionRequestException
import br.com.auth.exceptions.ObjectDuplicateException
import br.com.auth.exceptions.OperationUnauthorizedException
import br.com.auth.exceptions.ResourceNotFoundException
import br.com.auth.repository.RecoverPasswordRepository
import br.com.auth.repository.SecurityRepository
import br.com.auth.repository.UserRepository
import br.com.auth.security.JwtTokenProvider
import br.com.auth.utils.generateCode
import br.com.auth.utils.getLocalDateTime
import br.com.auth.utils.getLocalDateTimeAndPlusMinutes
import br.com.auth.utils.verifyLocalDateTimeIsValid
import br.com.auth.vo.user.EmailRequestVO
import br.com.auth.vo.user.NewPasswordRequestVO
import br.com.auth.vo.user.SignInRequestVO
import br.com.auth.vo.user.SignUpRequestVO
import br.com.auth.vo.user.TokenResponseVO
import br.com.auth.utils.CoreUtils.CODE_NOT_FOUND
import br.com.auth.utils.CoreUtils.EMAIL_NOT_FOUND
import br.com.auth.utils.CoreUtils.EXPIRED_CODE
import br.com.auth.utils.CoreUtils.SUBJECT
import br.com.auth.utils.CoreUtils.SUBJECT_RECOVER_PASSWORD
import br.com.auth.utils.CoreUtils.THE_EMAIL_ALREADY_EXISTS
import br.com.auth.utils.CoreUtils.THE_USER_ALREADY_EXISTS
import br.com.auth.utils.sendRecoverPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.swing.text.html.HTML.Tag.BODY

@Service
class AuthService {

    @Autowired
    private lateinit var securityRepository: SecurityRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var recoverPasswordRepository: RecoverPasswordRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var emailService: EmailService

    @Value("\${email.password-recover.token.minutes}")
    private val tokenMinutes: Long = 0

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Transactional
    fun confirmEmailAddress(
        emailRequestVO: EmailRequestVO
    ) {
        val checkUserAlreadyExists: UserDetails? = userRepository.findByEmail(email = emailRequestVO.email)
        if (checkUserAlreadyExists != null) {
            throw ObjectDuplicateException(message = THE_EMAIL_ALREADY_EXISTS)
        } else {
            val checkEmailAlreadyExists: Security? =
                securityRepository.checkEmailAlreadyExists(email = emailRequestVO.email)
            if (checkEmailAlreadyExists != null) {
                val code = generateCode()
                securityRepository.updateCodeVerificationEmail(
                    code = code,
                    expiration = getLocalDateTimeAndPlusMinutes(minutes = tokenMinutes),
                    email = emailRequestVO.email
                )
                emailService.sendEmailToConfirmation(
                    to = emailRequestVO.email, subject = SUBJECT, body = "$BODY: $code"
                )
            } else {
                val saveNewKeySecurity = Security(
                    code = generateCode(),
                    email = emailRequestVO.email,
                    expirationDate = getLocalDateTimeAndPlusMinutes(minutes = tokenMinutes)
                )
                securityRepository.save(saveNewKeySecurity)
                emailService.sendEmailToConfirmation(
                    to = emailRequestVO.email, subject = SUBJECT, body = "$BODY: ${saveNewKeySecurity.code}"
                )
            }
        }
    }

    @Transactional(readOnly = true)
    fun checkCodeSendToConfirmEmail(
        code: String
    ) {
        val entity: Security? = securityRepository.checkCodeSend(code = code)
        if (entity != null) {
            verifyLocalDateTimeIsValid(dateTime = entity.expirationDate)
        } else {
            throw ResourceNotFoundException(message = CODE_NOT_FOUND)
        }
    }

    @Transactional
    fun signUp(
        signUpRequestVO: SignUpRequestVO
    ) {
        val security: Security? = securityRepository.checkEmailAlreadyExists(email = signUpRequestVO.email)
        if (security != null) {
            val userDetails: UserDetails? = userRepository.findByEmail(email = signUpRequestVO.email)
            if (userDetails != null) {
                throw ObjectDuplicateException(message = THE_USER_ALREADY_EXISTS)
            } else {
                val userCreated = User()
                userCreated.createdAt = getLocalDateTime()
                userCreated.email = signUpRequestVO.email
                userCreated.password = passwordEncoder.encode(signUpRequestVO.password)
                userCreated.typeAccount = signUpRequestVO.typeAccount
                userCreated.security = security
                userRepository.save(userCreated)
            }
        } else {
            throw OperationUnauthorizedException(message = EMAIL_NOT_VERIFIED)
        }
    }

    @Transactional(readOnly = true)
    fun signIn(
        signInRequestVO: SignInRequestVO
    ): TokenResponseVO {
        return try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(signInRequestVO.email, signInRequestVO.password)
            )
            val user: User? = userRepository.fetchByEmail(email = signInRequestVO.email)
            tokenProvider.createAccessToken(username = authentication.name, typeAccount = user?.typeAccount!!)
        } catch (e: AuthenticationException) {
            throw ForbiddenActionRequestException(message = "invalid credentials")
        }
    }

    @Transactional
    fun createRecoverPassword(
        emailRequestVO: EmailRequestVO
    ) {
        val user: User? = userRepository.fetchByEmail(email = emailRequestVO.email)
        user?.let {
            val recoverPassword: RecoverPassword? =
                recoverPasswordRepository.findRecoverPasswordByEmail(email = it.email)
            if (recoverPassword != null) {
                val token = generateCode()
                val expiration = getLocalDateTimeAndPlusMinutes(minutes = tokenMinutes)
                recoverPasswordRepository.updateTokenAndDataExpiration(
                    email = recoverPassword.email, token = token, expiration = expiration
                )
                val text = sendRecoverPassword(code = token, minutes = tokenMinutes)
                emailService.sendEmailToConfirmation(
                    to = emailRequestVO.email,
                    subject = SUBJECT_RECOVER_PASSWORD,
                    body = text
                )
            } else {
                val code = generateCode()
                val expiration: LocalDateTime = getLocalDateTimeAndPlusMinutes(minutes = tokenMinutes)
                val recover = RecoverPassword(
                    code = code, email = emailRequestVO.email, expiration = expiration
                )
                recoverPasswordRepository.save(recover)
                val text = sendRecoverPassword(code = code, minutes = tokenMinutes)
                emailService.sendEmailToConfirmation(
                    to = emailRequestVO.email,
                    subject = SUBJECT_RECOVER_PASSWORD,
                    body = text
                )
            }
        } ?: throw ResourceNotFoundException(message = EMAIL_NOT_FOUND)
    }

    @Transactional
    fun checkRecoverPassword(
        code: String
    ) {
        val checkCodeAlreadyExists: RecoverPassword? = recoverPasswordRepository.checkCodeAlreadyExists(code = code)
        if (checkCodeAlreadyExists != null) {
            ResponseEntity.noContent()
        } else {
            throw ResourceNotFoundException(message = CODE_NOT_FOUND)
        }
    }

    @Transactional
    fun saveNewPassword(
        passwordRequestVO: NewPasswordRequestVO
    ) {
        val recoverPassword: RecoverPassword? =
            recoverPasswordRepository.findRecoverPasswordByEmail(email = passwordRequestVO.email)
        if (recoverPassword != null) {
            changePassword(recoverPassword = recoverPassword, passwordVO = passwordRequestVO)
        } else {
            throw ResourceNotFoundException(message = EMAIL_NOT_FOUND)
        }
    }

    @Transactional
    fun changePassword(
        recoverPassword: RecoverPassword,
        passwordVO: NewPasswordRequestVO
    ) {
        if (recoverPassword.expiration?.isAfter(getLocalDateTime()) == true) {
            val userInstanced: User? = userRepository.fetchByEmail(email = passwordVO.email)
            if (userInstanced != null) {
                userInstanced.password = passwordEncoder.encode(passwordVO.password)
                userRepository.save(userInstanced)
            } else {
                throw ResourceNotFoundException(message = EMAIL_NOT_FOUND)
            }
        } else {
            throw ForbiddenActionRequestException(message = EXPIRED_CODE)
        }
    }

    @Transactional(readOnly = true)
    fun refreshToken(
        email: String,
        refreshToken: String
    ): TokenResponseVO {
        val user: User? = userRepository.fetchByEmail(email = email)
        val tokenResponse: TokenResponseVO = if (user != null) {
            tokenProvider.refreshToken(refreshToken = refreshToken)
        } else {
            throw UsernameNotFoundException("Username $email not found")
        }
        return (tokenResponse)
    }

    companion object {
        const val EMAIL_NOT_VERIFIED = "Email Not Verified!"
    }
}
